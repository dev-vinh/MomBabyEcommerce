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
import java.util.List;

// category của bĩm tả id là 4
@WebServlet(name = "Search_Bimta", value = "/Search_Bimta")
public class Search_Bimta extends HttpServlet {
    ProductService productService = new ProductService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productService.getProductsByCategory(2);
        List<Product> topProducts = productService.getTopProductsByCategory(2, 4);

        request.setAttribute("products", products);
        request.setAttribute("topProducts", topProducts);
        request.setAttribute("categoryId", 2);

        request.getRequestDispatcher("search/search-clothings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xử lý yêu cầu POST ở đây
    }

}
