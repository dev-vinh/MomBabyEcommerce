package hcmuaf.fit.mombabyecommerce.controller.admin.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/delete-product")
public class DeleteProductController extends HttpServlet {
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService(DBConnection.getJdbi());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        try {
            ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
        Map<String, Object> body = mapper.readValue(request.getReader(), Map.class);

            Object idObj = body.get("productId");
            if (idObj == null) {
                response.setStatus(400);
                response.getWriter().write("{\"statusCode\":400,\"message\":\"Thiếu productId\"}");
                return;
            }

            int productId;
            if (idObj instanceof Number) {
                productId = ((Number) idObj).intValue();
            } else {
                productId = Integer.parseInt(idObj.toString());
            }

            boolean ok = productService.deactivateProduct(productId);
            if (ok) {
                response.setStatus(200);
                response.getWriter().write("{\"statusCode\":200,\"message\":\"Đã tắt hoạt động sản phẩm\"}");
            } else {
                response.setStatus(404);
                response.getWriter().write("{\"statusCode\":404,\"message\":\"Không tìm thấy sản phẩm\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"statusCode\":500,\"message\":\"Lỗi: " + e.getMessage() + "\"}");
        }
    }
}
