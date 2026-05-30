package hcmuaf.fit.mombabyecommerce.controller.admin;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.DashboardOrder;
import hcmuaf.fit.mombabyecommerce.model.DashboardStats;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.service.DashboardService;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DashboardController", value = "/admin/dashboard")
public class DashboardController extends HttpServlet {
    ProductService productService = new ProductService(DBConnection.getJdbi());
    DashboardService dashboardService = new DashboardService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String period = request.getParameter("period");

        if(period == null || period.isEmpty()) {
            period = "MONTH";
        }
        period = period.toUpperCase();
        List<Product> products = productService.getTop10();
        request.setAttribute("top10", products);
        request.setAttribute("period", period);

        request.setAttribute(
                "currentRevenue",
                dashboardService.getRevenue(period)
        );

        request.setAttribute(
                "currentOrders",
                dashboardService.getOrders(period)
        );

        request.setAttribute("totalProducts", dashboardService.getTotalProducts());
        request.setAttribute("totalCustomers", dashboardService.getTotalCustomers());

        List<DashboardStats> revenueChart = dashboardService.getRevenueChart(period);

        request.setAttribute("revenueChart", revenueChart);

        List<DashboardOrder> recentOrders = dashboardService.getRecentOrders();
        request.setAttribute("recentOrders", recentOrders);

        request.setAttribute("lowStockCount", dashboardService.getLowStockCount());
        request.setAttribute("outOfStockCount", dashboardService.getOutOfStockCount());

        List<Product> lowStockProducts = dashboardService.getLowStockProducts();
        request.setAttribute("lowStockProducts", lowStockProducts);
        request.setAttribute("revenueGrowth", dashboardService.getRevenueGrowth(period));
        request.setAttribute("ordersGrowth", dashboardService.getOrdersGrowth(period));

        request.setAttribute("lastRevenue", dashboardService.getLastRevenue(period));
        request.setAttribute("lastOrders", dashboardService.getLastOrders(period));
        request.setAttribute("monthlyRevenue", dashboardService.getMonthlyRevenue());

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
