package hcmuaf.fit.mombabyecommerce.controller.product;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

            Integer brandId = rootNode.has("brandId") && !rootNode.path("brandId").isNull()
                    ? rootNode.path("brandId").asInt()
                    : null;

            String sort = rootNode.has("sort")
                    ? rootNode.path("sort").asText()
                    : "default";

            Integer page = rootNode.has("page") && !rootNode.path("page").isNull()
                    ? rootNode.path("page").asInt()
                    : 1;

            Integer size = rootNode.has("size") && !rootNode.path("size").isNull()
                    ? rootNode.path("size").asInt()
                    : 16;
            int totalProducts = productService.countProducts(
                    categoryId,
                    minPrice,
                    maxPrice,
                    brandId
            );
            int totalPages = (int) Math.ceil((double) totalProducts / size);
            List<Product> filteredProducts = productService.filterProducts(
                    categoryId, minPrice, maxPrice, brandId, sort, page, size
            );

            ObjectNode result = objectMapper.createObjectNode();
            result.putPOJO("products", filteredProducts);
            result.put("totalPages", totalPages);
            result.put("currentPage", page);
            result.put("totalProducts", totalProducts);

            objectMapper.writeValue(response.getWriter(), result);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
