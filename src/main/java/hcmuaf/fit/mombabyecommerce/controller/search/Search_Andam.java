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

// category của Search_Andamtả id là 6
@WebServlet(name = "Search_Andam", value = "/Search_Andam")
public class Search_Andam extends HttpServlet {
    ProductService productService = new ProductService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productService.getProductsByCategory(3);
        List<Product> topProducts = productService.getTopProductsByCategory(3, 4);

        request.setAttribute("products", products);
        request.setAttribute("topProducts", topProducts);
        request.setAttribute("categoryId", 3);

        request.getRequestDispatcher("search/search-clothings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xử lý yêu cầu POST ở đây
    }
}
