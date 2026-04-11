package hcmuaf.fit.mombabyecommerce.controller.product;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebServlet(name = "ProductFilterController", value = "/product/filter")
public class ProductFilterController extends HttpServlet {
    private final ProductService productService = new ProductService(DBConnection.getJdbi());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            JsonNode rootNode = objectMapper.readTree(request.getReader());
            int categoryId = rootNode.path("category_id").asInt();
            Integer minPrice = rootNode.has("minPrice") && !rootNode.path("minPrice").isNull()
                    ? rootNode.path("minPrice").asInt()
                    : null;
            Integer maxPrice = rootNode.has("maxPrice") && !rootNode.path("maxPrice").isNull()
                    ? rootNode.path("maxPrice").asInt()
                    : null;

            List<Product> filteredProducts = productService.filterProducts(categoryId, minPrice, maxPrice);

            objectMapper.writeValue(response.getWriter(), filteredProducts);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
