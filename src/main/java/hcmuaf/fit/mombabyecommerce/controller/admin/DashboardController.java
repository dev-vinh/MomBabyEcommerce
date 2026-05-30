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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String period    = request.getParameter("period");
        String fromParam = request.getParameter("from");
        String toParam   = request.getParameter("to");

        int currentRevenue, currentOrders, currentCancelled;
        List<DashboardStats> revenueChart;
        String filterMode;

        boolean isRange = fromParam != null && !fromParam.isEmpty()
                && toParam   != null && !toParam.isEmpty();

        if (isRange) {
            filterMode     = "range";
            currentRevenue  = dashboardService.getRevenueByRange(fromParam, toParam);
            currentOrders   = dashboardService.getOrdersByRange(fromParam, toParam);
            currentCancelled= dashboardService.getCancelledByRange(fromParam, toParam);
            revenueChart    = dashboardService.getRevenueChartByRange(fromParam, toParam);
            request.setAttribute("selectedFrom", fromParam);
            request.setAttribute("selectedTo",   toParam);

        } else {
            filterMode = "period";
            if (period == null || period.isEmpty()) period = "MONTH";
            period = period.toUpperCase();
            currentRevenue   = dashboardService.getRevenue(period);
            currentOrders    = dashboardService.getOrders(period);
            currentCancelled = dashboardService.getCancelled(period);
            revenueChart     = dashboardService.getRevenueChart(period);
            request.setAttribute("revenueGrowth",   dashboardService.getRevenueGrowth(period));
            request.setAttribute("ordersGrowth",     dashboardService.getOrdersGrowth(period));
            request.setAttribute("cancelledGrowth",  dashboardService.getCancelledGrowth(period));
        }

        request.setAttribute("filterMode",      filterMode);
        request.setAttribute("period",           period != null ? period : "MONTH");
        request.setAttribute("currentRevenue",   currentRevenue);
        request.setAttribute("currentOrders",    currentOrders);
        request.setAttribute("currentCancelled", currentCancelled);
        request.setAttribute("revenueChart",     revenueChart);

        request.setAttribute("totalProducts",    dashboardService.getTotalProducts());
        request.setAttribute("totalCustomers",   dashboardService.getTotalCustomers());
        request.setAttribute("lowStockCount",    dashboardService.getLowStockCount());
        request.setAttribute("outOfStockCount",  dashboardService.getOutOfStockCount());
        request.setAttribute("lowStockProducts", dashboardService.getLowStockProducts());
        request.setAttribute("recentOrders",     dashboardService.getRecentOrders());
        request.setAttribute("top10",            productService.getTop10());

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}