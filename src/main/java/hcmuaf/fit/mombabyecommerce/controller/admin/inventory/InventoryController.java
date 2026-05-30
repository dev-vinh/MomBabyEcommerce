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
import jakarta.servlet.http.HttpSession;

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
            String pageParam = request.getParameter("page");
            String sizeParam = request.getParameter("size");

            int currentPage = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
            int size = (sizeParam != null) ? Integer.parseInt(sizeParam) : 10;
            if (size <= 0) size = 10;

            List<Product> products = productService.getAllProducts();
            List<OptionVariant> allOptions = optionService.getAllOptionsWithStock();
            for (OptionVariant option : allOptions) {

                int avgSoldPerMonth = optionService.getAverageSoldLast3Months(option.getId());

                int suggestedImport =
                        Math.max(0, avgSoldPerMonth - option.getStock());

                option.setSuggestedImport(suggestedImport);
            }
            Map<Integer, List<OptionVariant>> optionMap = allOptions.stream()
                    .collect(Collectors.groupingBy(OptionVariant::getProductId));

            List<InventoryDTO> inventoryList = optionMap.entrySet().stream()
                    .map(entry -> {
                        Integer productId = entry.getKey();
                        Product p = products.stream()
                                .filter(pr -> pr.getId().equals(productId))
                                .findFirst()
                                .orElse(null);
                        String name = p != null ? p.getName() : "Sản phẩm #" + productId;
                        String image = p != null ? p.getImageUrl() : null;
                        return new InventoryDTO(productId, name, image, entry.getValue());
                    })
                    .sorted(Comparator.comparingInt((InventoryDTO dto) ->
                            dto.getOptions().stream()
                                    .mapToInt(OptionVariant::getStock)
                                    .min()
                                    .orElse(Integer.MAX_VALUE)
                    ))
                    .collect(Collectors.toList());
            int totalItems = inventoryList.size();
            int totalPages = (int) Math.ceil((double) totalItems / size);
            if (totalPages < 1) totalPages = 1;
            if (currentPage < 1) currentPage = 1;
            if (currentPage > totalPages) currentPage = totalPages;

            int fromIndex = (currentPage - 1) * size;
            int toIndex = Math.min(fromIndex + size, totalItems);
            List<InventoryDTO> pagedList = (fromIndex < totalItems)
                    ? inventoryList.subList(fromIndex, toIndex)
                    : Collections.emptyList();

            request.setAttribute("inventoryList", pagedList);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("size", size);
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
            String reason = body.has("reason") ? body.get("reason").asText(null) : null;

            if (quantity == null || quantity < 0) {
                writeJson(response, new ResponseWrapper<>(400, "error", "Số lượng không hợp lệ.", null));
                return;
            }

            Integer userId = null;
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object uid = session.getAttribute("userId");
                if (uid instanceof Integer) userId = (Integer) uid;
            }

            boolean updated = optionService.updateStockWithLocation(optionVariantId, quantity, location, userId, reason);
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