package hcmuaf.fit.mombabyecommerce.controller.admin.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Variant;
import hcmuaf.fit.mombabyecommerce.service.VariantService;
import hcmuaf.fit.mombabyecommerce.util.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "VariantController", urlPatterns = {"/admin/api/variants", "/admin/api/variants/*"})
public class VariantController extends HttpServlet {

    private final VariantService variantService = new VariantService(DBConnection.getJdbi());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            String pathInfo = request.getPathInfo();

            // URL dạng: /admin/api/variants/1
            // 1 ở đây là attributes.id
            if (pathInfo != null && pathInfo.length() > 1) {
                Integer attributeId = Integer.parseInt(pathInfo.substring(1));

                List<Variant> values = variantService.getVariantValuesByAttributeId(attributeId);

                writeResponse(response, new ResponseWrapper<>(
                        HttpServletResponse.SC_OK,
                        "success",
                        "Lấy giá trị biến thể thành công.",
                        values
                ));
                return;
            }

            // URL dạng: /admin/api/variants?categoryId=1
            Integer categoryId = null;
            String categoryIdParam = request.getParameter("categoryId");

            if (categoryIdParam != null && !categoryIdParam.trim().isEmpty()) {
                categoryId = Integer.parseInt(categoryIdParam);
            }

            List<Variant> variants = variantService.getVariantsByCategoryId(categoryId);

            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_OK,
                    "success",
                    "Lấy danh sách thuộc tính thành công.",
                    variants
            ));

        } catch (NumberFormatException e) {
            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "error",
                    "Mã thuộc tính không hợp lệ.",
                    null
            ));
        } catch (Exception e) {
            e.printStackTrace();
            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "error",
                    "Lỗi lấy dữ liệu biến thể: " + e.getMessage(),
                    null
            ));
        }
    }

    private void writeResponse(HttpServletResponse response, ResponseWrapper<?> responseWrapper) throws IOException {
        response.setStatus(responseWrapper.getStatusCode());
        response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
    }
}