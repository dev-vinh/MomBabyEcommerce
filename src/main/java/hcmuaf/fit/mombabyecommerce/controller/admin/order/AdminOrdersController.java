package hcmuaf.fit.mombabyecommerce.controller.admin.order;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Order;
import hcmuaf.fit.mombabyecommerce.service.ExcelExportService;
import hcmuaf.fit.mombabyecommerce.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminOrdersController", value = "/admin/orders")
public class AdminOrdersController extends HttpServlet {
    OrderService orderService = new OrderService(DBConnection.getJdbi());
    ExcelExportService excelExportService = new ExcelExportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("export".equals(action)) {
            try {
                List<Order> orders = orderService.getAllOrders();
                byte[] excelBytes = excelExportService.exportOrdersToExcel(orders);

                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=orders.xlsx");
                response.getOutputStream().write(excelBytes);
                response.getOutputStream().flush();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error exporting orders to Excel");
                return;
            }
        }
        List<Order> orders = orderService.getAllOrders();
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("orders.jsp").forward(request, response);
    }
}
