package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.DashboardDao;
import hcmuaf.fit.mombabyecommerce.model.DashboardOrder;
import hcmuaf.fit.mombabyecommerce.model.DashboardStats;
import hcmuaf.fit.mombabyecommerce.model.Product;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class DashboardService {

    private final DashboardDao dashboardDAO;

    public DashboardService(Jdbi jdbi) {
        this.dashboardDAO = jdbi.onDemand(DashboardDao.class);
    }

    public int getRevenue(String period) {
        Integer revenue;
        switch (period.toUpperCase()) {
            case "WEEK":
                revenue = dashboardDAO.getCurrentWeekRevenue();
                break;

            case "YEAR":
                revenue = dashboardDAO.getCurrentYearRevenue();
                break;
            case "MONTH":
            default:
                revenue = dashboardDAO.getCurrentMonthRevenue();
                break;
        }
        return revenue != null ? revenue : 0;
    }

    public int getOrders(String period) {
        Integer orders;
        switch (period.toUpperCase()) {
            case "WEEK":
                orders = dashboardDAO.getCurrentWeekOrders();
                break;
            case "YEAR":
                orders = dashboardDAO.getCurrentYearOrders();
                break;
            case "MONTH":
            default:
                orders = dashboardDAO.getCurrentMonthOrders();
                break;
        }
        return orders != null ? orders : 0;
    }

    public int getTotalProducts() {
        Integer total = dashboardDAO.getTotalProducts();
        return total != null ? total : 0;
    }

    public int getTotalCustomers() {
        Integer customers = dashboardDAO.getTotalCustomers();
        return customers != null ? customers : 0;
    }

    public int getLowStockCount() {
        Integer count = dashboardDAO.getLowStockCount();
        return count != null ? count : 0;
    }

    public int getOutOfStockCount() {
        Integer count = dashboardDAO.getOutOfStockCount();
        return count != null ? count : 0;
    }

    public List<DashboardStats> getRevenueChart(String period) {

        switch (period.toUpperCase()) {

            case "WEEK":
                return dashboardDAO.getCurrentWeekRevenueChart();

            case "YEAR":
                return dashboardDAO.getCurrentYearRevenueChart();

            case "MONTH":
            default:
                return dashboardDAO.getCurrentMonthRevenueChart();
        }
    }

    public List<DashboardOrder> getRecentOrders() {
        return dashboardDAO.getRecentOrders();
    }

    public List<Product> getLowStockProducts() {
        return dashboardDAO.getLowStockProducts();
    }

    public int getLastRevenue(String period) {
        Integer revenue;
        switch (period.toUpperCase()) {
            case "WEEK":
                revenue = dashboardDAO.getLastWeekRevenue();
                break;
            case "YEAR":
                revenue = dashboardDAO.getLastYearRevenue();
                break;
            case "MONTH":
            default:
                revenue = dashboardDAO.getLastMonthRevenue();
                break;
        }
        return revenue != null ? revenue : 0;
    }

    public int getLastOrders(String period) {
        Integer orders;
        switch (period.toUpperCase()) {
            case "WEEK":
                orders = dashboardDAO.getLastWeekOrders();
                break;
            case "YEAR":
                orders = dashboardDAO.getLastYearOrders();
                break;
            case "MONTH":
            default:
                orders = dashboardDAO.getLastMonthOrders();
                break;
        }
        return orders != null ? orders : 0;
    }

    public double getRevenueGrowth(String period) {
        int current = getRevenue(period);
        int last = getLastRevenue(period);
        if (last == 0) return current > 0 ? 100.0 : 0.0;
        return Math.round(((double)(current - last) / last) * 10000.0) / 100.0;
    }

    public double getOrdersGrowth(String period) {
        int current = getOrders(period);
        int last = getLastOrders(period);
        if (last == 0) return current > 0 ? 100.0 : 0.0;
        return Math.round(((double)(current - last) / last) * 10000.0) / 100.0;
    }

    public List<DashboardStats> getMonthlyRevenue() {
        return dashboardDAO.getMonthlyRevenue();
    }

    public int getRevenueByRange(String from, String to) {
        Integer r = dashboardDAO.getRevenueByRange(from, to);
        return r != null ? r : 0;
    }

    public int getOrdersByRange(String from, String to) {
        Integer r = dashboardDAO.getOrdersByRange(from, to);
        return r != null ? r : 0;
    }


    public List<DashboardStats> getRevenueChartByRange(String from, String to) {
        return dashboardDAO.getRevenueChartByRange(from, to);
    }
    public int getCancelled(String period) {
        Integer c;
        switch (period.toUpperCase()) {
            case "WEEK":  c = dashboardDAO.getCurrentWeekCancelled();  break;
            case "YEAR":  c = dashboardDAO.getCurrentYearCancelled();  break;
            default:      c = dashboardDAO.getCurrentMonthCancelled(); break;
        }
        return c != null ? c : 0;
    }

    public int getLastCancelled(String period) {
        Integer c;
        switch (period.toUpperCase()) {
            case "WEEK":  c = dashboardDAO.getLastWeekCancelled();  break;
            case "YEAR":  c = dashboardDAO.getLastYearCancelled();  break;
            default:      c = dashboardDAO.getLastMonthCancelled(); break;
        }
        return c != null ? c : 0;
    }

    public double getCancelledGrowth(String period) {
        int current = getCancelled(period);
        int last    = getLastCancelled(period);
        if (last == 0) return current > 0 ? 100.0 : 0.0;
        return Math.round(((double)(current - last) / last) * 10000.0) / 100.0;
    }

    public int getCancelledByRange(String from, String to) {
        Integer c = dashboardDAO.getCancelledByRange(from, to);
        return c != null ? c : 0;
    }



}