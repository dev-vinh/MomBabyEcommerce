package hcmuaf.fit.mombabyecommerce.controller.admin.inventory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.InventoryDTO;
import hcmuaf.fit.mombabyecommerce.model.OptionVariant;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.service.OptionService;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import hcmuaf.fit.mombabyecommerce.util.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "InventoryController",
        urlPatterns = {"/admin/inventory", "/admin/api/inventory", "/admin/api/inventory/*"})
public class InventoryController extends HttpServlet {

    private final OptionService optionService = new OptionService(DBConnection.getJdbi());
    private final ProductService productService = new ProductService(DBConnection.getJdbi());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (uri.contains("/api/inventory")) {
            response.setContentType("application/json;charset=UTF-8");
            try {
                String productIdParam = request.getParameter("productId");
                if (productIdParam == null) {
                    writeJson(response, new ResponseWrapper<>(400, "error", "Thiếu productId", null));
                    return;
                }
                Integer productId = Integer.parseInt(productIdParam);
                List<OptionVariant> options = optionService.getOptionsWithStockByProductId(productId);
                writeJson(response, new ResponseWrapper<>(200, "success", "OK", options));
            } catch (Exception e) {
                writeJson(response, new ResponseWrapper<>(500, "error", e.getMessage(), null));
            }
            return;
        }

        try {
            List<Product> products = productService.getAllProducts();
            List<OptionVariant> allOptions = optionService.getAllOptionsWithStock();

            // Group options theo productId
            Map<Integer, List<OptionVariant>> optionMap = allOptions.stream()
                    .collect(Collectors.groupingBy(OptionVariant::getProductId));

            List<InventoryDTO> inventoryList = products.stream()
                    .filter(p -> optionMap.containsKey(p.getId()))
                    .map(p -> new InventoryDTO(
                            p.getId(),
                            p.getName(),
                            p.getImageUrl(),
                            optionMap.getOrDefault(p.getId(), new ArrayList<>())
                    ))
                    .collect(Collectors.toList());

            request.setAttribute("inventoryList", inventoryList);
            request.getRequestDispatcher("/admin/inventory.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Lỗi tải trang quản lý kho: " + e.getMessage());
        }
    }

    // PUT /admin/api/inventory/{optionVariantId}  → cập nhật stock
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        try {
            Integer optionVariantId = parsePathId(request.getPathInfo());
            JsonNode body = objectMapper.readTree(request.getReader());

            Integer quantity = body.has("quantity") ? body.get("quantity").asInt() : null;
            String location = body.has("location") ? body.get("location").asText(null) : null;

            if (quantity == null || quantity < 0) {
                writeJson(response, new ResponseWrapper<>(400, "error", "Số lượng không hợp lệ.", null));
                return;
            }

            boolean updated = optionService.updateStockWithLocation(optionVariantId, quantity, location);
            if (updated) {
                OptionVariant option = optionService.getOptionById(optionVariantId);
                writeJson(response, new ResponseWrapper<>(200, "success", "Cập nhật kho thành công.", option));
            } else {
                writeJson(response, new ResponseWrapper<>(404, "error", "Không tìm thấy bản ghi kho.", null));
            }

        } catch (NumberFormatException e) {
            writeJson(response, new ResponseWrapper<>(400, "error", "ID không hợp lệ.", null));
        } catch (Exception e) {
            e.printStackTrace();
            writeJson(response, new ResponseWrapper<>(500, "error", "Lỗi cập nhật kho: " + e.getMessage(), null));
        }
    }

    private Integer parsePathId(String pathInfo) {
        if (pathInfo == null || pathInfo.length() <= 1) throw new IllegalArgumentException("Thiếu ID.");
        return Integer.parseInt(pathInfo.substring(1));
    }

    private void writeJson(HttpServletResponse response, ResponseWrapper<?> wrapper) throws IOException {
        response.setStatus(wrapper.getStatusCode());
        response.getWriter().write(objectMapper.writeValueAsString(wrapper));
    }
}