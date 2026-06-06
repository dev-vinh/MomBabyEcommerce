package hcmuaf.fit.mombabyecommerce.controller.admin.product;

import com.fasterxml.jackson.databind.JsonNode;
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

        prepareJsonResponse(response);

        try {
            String pathInfo = request.getPathInfo();


            if (pathInfo != null && pathInfo.length() > 1) {
                Integer attributeId = parsePathId(pathInfo);

                List<Variant> values = variantService.getVariantValuesByAttributeId(attributeId);

                writeResponse(response, new ResponseWrapper<>(
                        HttpServletResponse.SC_OK,
                        "success",
                        "Lấy giá trị biến thể thành công.",
                        values
                ));
                return;
            }


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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        prepareJsonResponse(response);

        try {
            JsonNode body = objectMapper.readTree(request.getReader());

            Integer categoryId = readInteger(body, "categoryId");
            String name = readText(body, "name");

            Integer attributeId = readInteger(body, "attributeId");
            String value = readText(body, "value");

            Variant created;
            String message;

            if (name != null && !name.trim().isEmpty()) {
                created = variantService.createTemplateName(categoryId, name);
                message = "Thêm thuộc tính biến thể thành công.";
            } else {
                created = variantService.createTemplateValue(attributeId, value);
                message = "Thêm giá trị dropdown thành công.";
            }

            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_CREATED,
                    "success",
                    message,
                    created
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
                    "Lỗi thêm giá trị dropdown: " + e.getMessage(),
                    null
            ));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        prepareJsonResponse(response);

        try {
            Integer id = parsePathId(request.getPathInfo());

            JsonNode body = objectMapper.readTree(request.getReader());
            String value = readText(body, "value");

            Variant updated = variantService.updateTemplateValue(id, value);

            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_OK,
                    "success",
                    "Cập nhật giá trị dropdown thành công.",
                    updated
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
                    "Lỗi cập nhật giá trị dropdown: " + e.getMessage(),
                    null
            ));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        prepareJsonResponse(response);
        try {
            Integer id = parsePathId(request.getPathInfo());
            String scope = request.getParameter("scope");
            if ("name".equalsIgnoreCase(scope)) {
                variantService.deleteTemplateName(id);
                writeResponse(response, new ResponseWrapper<>(HttpServletResponse.SC_OK, "success", "Xóa thuộc tính biến thể thành công.", null));
                return;
            }

            variantService.deleteTemplateValue(id);
            writeResponse(response, new ResponseWrapper<>(HttpServletResponse.SC_OK, "success", "Xóa giá trị dropdown thành công.", null));
        } catch (IllegalArgumentException e) {
            writeResponse(response, new ResponseWrapper<>(HttpServletResponse.SC_BAD_REQUEST, "error", e.getMessage(), null));
        } catch (Exception e) {

            e.printStackTrace();
            writeResponse(response, new ResponseWrapper<>(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Lỗi xóa giá trị dropdown: " + e.getMessage(), null));
        }
    }

    private Integer parsePathId(String pathInfo) {
        if (pathInfo == null || pathInfo.length() <= 1) {
            throw new IllegalArgumentException("Thiếu mã dữ liệu biến thể.");
        }

        return Integer.parseInt(pathInfo.substring(1));
    }

    private Integer readInteger(JsonNode body, String fieldName) {
        JsonNode node = body == null ? null : body.get(fieldName);

        if (node == null || node.isNull()) {
            return null;
        }

        return node.asInt();
    }

    private String readText(JsonNode body, String fieldName) {
        JsonNode node = body == null ? null : body.get(fieldName);

        if (node == null || node.isNull()) {
            return null;
        }

        return node.asText();
    }

    private void prepareJsonResponse(HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

    private void writeResponse(HttpServletResponse response, ResponseWrapper<?> responseWrapper) throws IOException {
        response.setStatus(responseWrapper.getStatusCode());
        response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
    }
}