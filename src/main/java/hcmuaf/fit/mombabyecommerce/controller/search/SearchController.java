package hcmuaf.fit.mombabyecommerce.controller.search;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.service.ProductService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "SearchController", value = "/search-results")
public class SearchController extends HttpServlet {
    private final ProductService productService = new ProductService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("name");

        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Product> products = productService.searchProducts(keyword);
            request.setAttribute("products", products);
            request.setAttribute("productCount", products.size());
        }else {
            keyword = "";
            request.setAttribute("products", Collections.emptyList());
            request.setAttribute("productCount", 0);
        }

        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("search/search-results.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
