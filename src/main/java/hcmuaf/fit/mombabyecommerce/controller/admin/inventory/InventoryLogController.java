package hcmuaf.fit.mombabyecommerce.controller.admin.inventory;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.InventoryLog;
import hcmuaf.fit.mombabyecommerce.service.OptionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "InventoryLogController",
        urlPatterns = {"/admin/inventory-log"})
public class InventoryLogController extends HttpServlet {

    private final OptionService optionService = new OptionService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String pageParam = request.getParameter("page");
            String sizeParam = request.getParameter("size");
            String productIdParam = request.getParameter("productId");
            String actionType = request.getParameter("actionType");
            String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");

            int currentPage = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
            int size = (sizeParam != null) ? Integer.parseInt(sizeParam) : 10;
            if (size <= 0) size = 10;

            Integer productId = (productIdParam != null && !productIdParam.isBlank())
                    ? Integer.parseInt(productIdParam) : null;

            List<InventoryLog> logs = optionService.getLogsPaged(
                    productId, actionType, fromDate, toDate, currentPage, size);
            int totalItems = optionService.countLogs(productId, actionType, fromDate, toDate);
            int totalPages = (int) Math.ceil((double) totalItems / size);
            if (totalPages < 1) totalPages = 1;
            if (currentPage < 1) currentPage = 1;
            if (currentPage > totalPages) currentPage = totalPages;

            request.setAttribute("logs", logs);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("size", size);
            request.setAttribute("filterProductId", productIdParam);
            request.setAttribute("filterActionType", actionType);
            request.setAttribute("filterFromDate", fromDate);
            request.setAttribute("filterToDate", toDate);

            request.getRequestDispatcher("/admin/inventory-log.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Lỗi tải trang lịch sử kho: " + e.getMessage());
        }
    }
}
