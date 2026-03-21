package hcmuaf.fit.mombabyecommerce.controller.search;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import vn.edu.hcmuaf.fit.trang_web_ban_hang_san_pham_cho_me_va_be.util.ResponseWrapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/home/products/search")
public class SearchProductController extends HttpServlet{
    private final ProductService productService = new ProductService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String limitParam = request.getParameter("limit");

        if (name == null || name.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(new ResponseWrapper<>(400, "error", "Keyword must not be empty", null).toJson());
            return;
        }

        // Lấy danh sách sản phẩm từ service
        List<Product> products = productService.searchProducts(name);

        if (products.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            response.getWriter().write(new ResponseWrapper<>(404, "error", "No products found", null).toJson());
            return;
        }

        // Xử lý giới hạn số lượng sản phẩm trả về
        int limit = 5; // Giá trị mặc định
        try {
            if (limitParam != null) {
                limit = Math.max(1, Integer.parseInt(limitParam)); // Tránh giá trị âm hoặc 0
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(new ResponseWrapper<>(400, "error", "Invalid limit value", null).toJson());
            return;
        }

        // Chỉ lấy tối đa `limit` sản phẩm
        List<Product> limitedProducts = products.stream()
                .limit(limit)
                .map(product -> new Product(
                        product.getId(),
                        product.getName(),
                        null, // Không cần sku
                        null, // Không cần description
                        null, // Không cần isActive
                        null, // Không cần categoryId
                        null, // Không cần brandId
                        null, // Không cần noOfViews
                        null, // Không cần noOfSold
                        product.getImageId(),
                        product.getPrice(),
                        product.getStock(),
                        product.getOptionId(),
                        null,// Không cần categoryName
                        product.getImageUrl()
                ))
                .collect(Collectors.toList());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(new ResponseWrapper<>(200, "success", "Products found", limitedProducts).toJson());
    }
}
