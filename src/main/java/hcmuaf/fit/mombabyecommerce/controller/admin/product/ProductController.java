package hcmuaf.fit.mombabyecommerce.controller.admin.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.ProductDTO;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import hcmuaf.fit.mombabyecommerce.util.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "AddProductController", value = {"/admin/products"})
public class ProductController extends HttpServlet {
    private final ProductService productService = new ProductService(DBConnection.getJdbi());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            Map<String, Object> productData = objectMapper.readValue(requestBody, Map.class);

            String name = getString(productData, "name");
            String sku = getString(productData, "sku");
            String description = getString(productData, "description");
            Integer categoryId = getInteger(productData, "categoryId");
            Integer brandId = getInteger(productData, "brandId");
            Integer imageId = getInteger(productData, "primaryImage");
            Boolean isActive = getBoolean(productData, "isActive", true);
            List<Integer> imageIds = getIntegerList(productData.get("imageIds"));
            List<Map<String, Object>> options = getMapList(productData.get("options"));

            validateProductInput(name, sku, categoryId, brandId, imageId, imageIds, options);

            ProductDTO createdProduct = productService.createProductForAdmin(
                    name,
                    sku,
                    description,
                    isActive,
                    categoryId,
                    brandId,
                    imageId,
                    imageIds,
                    options
            );

            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_CREATED,
                    "success",
                    "Thêm sản phẩm thành công.",
                    createdProduct
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
                    "Lỗi thêm sản phẩm: " + e.getMessage(),
                    null
            ));
        }
    }

    private void validateProductInput(String name,
                                      String sku,
                                      Integer categoryId,
                                      Integer brandId,
                                      Integer imageId,
                                      List<Integer> imageIds,
                                      List<Map<String, Object>> options) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }
        if (isBlank(sku)) {
            throw new IllegalArgumentException("SKU không được để trống.");
        }
        if (categoryId == null || categoryId <= 0) {
            throw new IllegalArgumentException("Vui lòng chọn danh mục hợp lệ.");
        }
        if (brandId == null || brandId <= 0) {
            throw new IllegalArgumentException("Vui lòng chọn thương hiệu hợp lệ.");
        }
        if (imageId == null || imageId <= 0 || imageIds == null || imageIds.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn ít nhất 1 ảnh sản phẩm.");
        }
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("Sản phẩm cần ít nhất 1 phiên bản bán.");
        }
    }

    private String getString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value == null ? "" : value.toString().trim();
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

    private Boolean getBoolean(Map<String, Object> data, String key, Boolean defaultValue) {
        Object value = data.get(key);
        if (value == null || value.toString().trim().isEmpty()) {
            return defaultValue;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return Boolean.parseBoolean(value.toString());
    }

    private List<Integer> getIntegerList(Object value) {
        if (!(value instanceof List<?> list)) {
            return null;
        }

        List<Integer> result = new ArrayList<>();
        for (Object item : list) {
            if (item == null || item.toString().trim().isEmpty()) {
                continue;
            }
            if (item instanceof Number) {
                result.add(((Number) item).intValue());
            } else {
                result.add(Integer.parseInt(item.toString().trim()));
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getMapList(Object value) {
        if (!(value instanceof List<?> list)) {
            return null;
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof Map<?, ?> rawMap) {
                result.add((Map<String, Object>) rawMap);
            }
        }
        return result;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void writeResponse(HttpServletResponse response, ResponseWrapper<?> responseWrapper) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(responseWrapper.getStatusCode());
        response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
    }
}
