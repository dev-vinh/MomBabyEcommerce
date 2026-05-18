package hcmuaf.fit.mombabyecommerce.controller.admin.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.ProductDTO;
import hcmuaf.fit.mombabyecommerce.service.ImageService;
import hcmuaf.fit.mombabyecommerce.service.OptionService;
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

@WebServlet(name = "EditProductController", value = "/admin/editProduct")
public class EditProductController extends HttpServlet {
    ProductService productService = new ProductService(DBConnection.getJdbi());
    ImageService imageService = new ImageService(DBConnection.getJdbi());
    OptionService optionService = new OptionService(DBConnection.getJdbi());
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            ProductDTO productDTO = productService.editProductById(productId);

            if (productDTO == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(new ResponseWrapper<>(
                        HttpServletResponse.SC_NOT_FOUND,
                        "error",
                        "Không tìm thấy sản phẩm.",
                        null
                ).toJson());
                return;
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ResponseWrapper<>(200, "Success", "Success", productDTO).toJson());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Internal Server Error\", \"details\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            Map<String, Object> productData = objectMapper.readValue(requestBody, Map.class);

            Integer productId = getInteger(productData, "id");
            String name = getString(productData, "name");
            String sku = getString(productData, "sku");
            String description = getString(productData, "description");
            Integer categoryId = getInteger(productData, "categoryId");
            Integer brandId = getInteger(productData, "brandId");
            Integer imageId = getInteger(productData, "primaryImage");
            Integer optionId = getInteger(productData, "optionId");
            Integer price = getInteger(productData, "price");
            Integer stock = getInteger(productData, "stock");
            Boolean isActive = getBoolean(productData, "isActive", true);
            List<Integer> variantIds = getIntegerList(productData.get("variantIds"));

            validateProductInput(productId, name, sku, categoryId, brandId, price, stock);

            ProductDTO updatedProduct = productService.updateProductForAdmin(
                    productId,
                    name,
                    sku,
                    description,
                    isActive,
                    categoryId,
                    brandId,
                    imageId,
                    optionId,
                    price,
                    stock,
                    variantIds
            );

            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_OK,
                    "success",
                    "Cập nhật sản phẩm thành công.",
                    updatedProduct
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
                    "Lỗi cập nhật sản phẩm: " + e.getMessage(),
                    null
            ));
        }
    }
    private void validateProductInput(Integer productId,
                                      String name,
                                      String sku,
                                      Integer categoryId,
                                      Integer brandId,
                                      Integer price,
                                      Integer stock) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Mã sản phẩm không hợp lệ.");
        }
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
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Giá bán phải lớn hơn 0.");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Số lượng tồn kho không được âm.");
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

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void writeResponse(HttpServletResponse response, ResponseWrapper<?> responseWrapper) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(responseWrapper.getStatusCode());
        response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
    }

}

