package hcmuaf.fit.mombabyecommerce.controller.search;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchCategory", value = "/search-category")
public class SearchCategory extends HttpServlet {

    private final ProductService productService =
            new ProductService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String categoryIdRaw = request.getParameter("categoryId");

        int categoryId;
        try {
            categoryId = Integer.parseInt(categoryIdRaw);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid categoryId");
            return;
        }

        List<Product> products = productService.getProductsByCategory(categoryId);
        List<Product> topProducts = productService.getTopProductsByCategory(categoryId, 4);

        request.setAttribute("products", products);
        request.setAttribute("topProducts", topProducts);
        request.setAttribute("categoryId", categoryId);

        request.getRequestDispatcher("search/search-clothings.jsp")
                .forward(request, response);
    }
}
