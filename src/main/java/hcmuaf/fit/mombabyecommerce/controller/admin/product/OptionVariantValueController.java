package hcmuaf.fit.mombabyecommerce.controller.admin.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.VariantService;
import hcmuaf.fit.mombabyecommerce.util.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/admin/addOptionVariantValue")
public class OptionVariantValueController extends HttpServlet {
    private final VariantService variantService = new VariantService(DBConnection.getJdbi());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            Map<String, Object> requestData = objectMapper.readValue(requestBody, Map.class);

            Integer optionId = getInteger(requestData, "optionId");
            Integer variantId = getInteger(requestData, "variantId");

            if (optionId == null || optionId <= 0) {
                throw new IllegalArgumentException("Mã phiên bản sản phẩm không hợp lệ.");
            }
            if (variantId == null || variantId <= 0) {
                throw new IllegalArgumentException("Mã giá trị biến thể không hợp lệ.");
            }

            int result = variantService.addOptionVariantValue(optionId, variantId);

            if (result <= 0) {
                throw new IllegalArgumentException("Không thể gán giá trị biến thể cho phiên bản sản phẩm.");
            }

            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_OK,
                    "success",
                    "Gán giá trị biến thể thành công.",
                    result
            ));
        } catch (IllegalArgumentException e) {
            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "error",
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            e.printStackTrace();
            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "error",
                    "Lỗi gán biến thể: " + e.getMessage(),
                    null
            ));
        }
    }

    private Integer getInteger(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null || value.toString().trim().isEmpty()) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(value.toString().trim());
    }

    private void writeResponse(HttpServletResponse response, ResponseWrapper<?> responseWrapper) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(responseWrapper.getStatusCode());
        response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
    }
}
