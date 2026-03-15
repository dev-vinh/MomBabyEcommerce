package hcmuaf.fit.mombabyecommerce.controller.admin.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.DeleteProductService;
import hcmuaf.fit.mombabyecommerce.util.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/delete-product")
public class DeleteProductController extends HttpServlet {
    private DeleteProductService productService;

    @Override
    public void init() throws ServletException {
        this.productService = new DeleteProductService(DBConnection.getJdbi());
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Đọc dữ liệu từ body của request
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }

        // Phân tích JSON (lấy productId)
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(sb.toString(), Map.class);
        String productIdString = (String) jsonMap.get("productId");  // lấy productId là String

        // Kiểm tra nếu productId là hợp lệ, chuyển nó sang Integer
        Integer productId = null;
        if (productIdString != null) {
            try {
                productId = Integer.valueOf(productIdString);  // Chuyển String sang Integer
            } catch (NumberFormatException e) {
                // Trường hợp khi không thể chuyển đổi giá trị thành Integer
                ResponseWrapper<String> responseWrapper = new ResponseWrapper<>(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "error",
                        "product_id không hợp lệ",
                        null
                );
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write(responseWrapper.toJson());
                return;
            }
        }
    }
}