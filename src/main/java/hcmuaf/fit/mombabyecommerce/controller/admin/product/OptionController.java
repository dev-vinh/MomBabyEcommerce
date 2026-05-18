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

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "OptionController", urlPatterns = {"/admin/options/create"})
public class OptionController extends HttpServlet {
    private final OptionService optionService = new OptionService(DBConnection.getJdbi());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            Map<String, Object> requestData = objectMapper.readValue(requestBody, Map.class);

            Integer productId = getInteger(requestData, "productId");
            Integer price = getInteger(requestData, "price");

            if (productId == null || productId <= 0) {
                throw new IllegalArgumentException("Mã sản phẩm không hợp lệ.");
            }
            if (price == null || price <= 0) {
                throw new IllegalArgumentException("Giá bán phải lớn hơn 0.");
            }

            int optionId = optionService.createOptions(productId, price);
            optionService.createInventory(optionId, 0);
            OptionVariant newOption = optionService.getOptionById(optionId);

            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_CREATED,
                    "success",
                    "Tạo phiên bản sản phẩm thành công.",
                    newOption
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
                    "Lỗi tạo phiên bản sản phẩm: " + e.getMessage(),
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
