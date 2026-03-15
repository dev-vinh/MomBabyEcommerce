package hcmuaf.fit.mombabyecommerce.controller.admin.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.OptionVariant;
import hcmuaf.fit.mombabyecommerce.service.OptionService;
import hcmuaf.fit.mombabyecommerce.util.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "OptionController", urlPatterns = {"/admin/options/create"})
public class OptionController  extends HttpServlet {
    private final OptionService optionService = new OptionService(DBConnection.getJdbi());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Đọc payload JSON từ request
            StringBuilder payload = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    payload.append(line);
                }
            }
            // Chuyển đổi JSON thành đối tượng Java
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> requestData = objectMapper.readValue(payload.toString(), Map.class);


            Integer productId = (Integer) requestData.get("productId");
            Integer price = (Integer) requestData.get("price");
            Integer stock = (Integer) requestData.get("stock");

            // Gọi service để tạo option
            int option = optionService.createOptions(productId, price, stock);
            OptionVariant newOption = optionService.getOptionById(option);


            ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(
                    201, "success", "Option created successfully", newOption);
            writeResponse(response, responseWrapper);

        } catch (Exception e) {
            ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(
                    500, "error", "Internal server error: " + e.getMessage(), null);
            writeResponse(response, responseWrapper);
        }
    }

    private void writeResponse(HttpServletResponse response, ResponseWrapper<?> responseWrapper) throws IOException {
        response.setContentType("application/json");
        response.setStatus(responseWrapper.getStatusCode());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
    }
}
