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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/addOptionVariantValue")
public class OptionVariantValueController  extends HttpServlet {
    private final VariantService variantService = new VariantService(DBConnection.getJdbi());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResponseWrapper<VariantService> responseWrapper;
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

            // Lấy giá trị từ JSON
            Integer optionId = (Integer) requestData.get("optionId");
            Object variantIdObj = requestData.get("variantId");

            Integer variantId = null;

            // Kiểm tra xem variantValueId là String hay Integer và chuyển đổi
            if (variantIdObj instanceof String) {
                variantId = Integer.parseInt((String) variantIdObj);
            } else if (variantIdObj instanceof Integer) {
                variantId = (Integer) variantIdObj;
            }

            int result = variantService.addOptionVariantValue(optionId, variantId);

            VariantService newOptionVariantValue = variantService;



            if (result > 0) {
                responseWrapper = new ResponseWrapper<>(200, "success", "OptionVariantValue added successfully.", newOptionVariantValue);
            } else {
                responseWrapper = new ResponseWrapper<>(500, "error", "Failed to add OptionVariantValue.", null);
            }


        } catch (Exception e) {
            ResponseWrapper<String> errorWrapper = new ResponseWrapper<>(500, "error", "An error occurred: " + e.getMessage(), null);

            //            writeResponse(response, errorWrapper);
        }


    }

}

