package hcmuaf.fit.mombabyecommerce.model;

import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

public class DashboardStats {
    private String label;
    private Integer revenue;
    private Integer totalRevenue;
    private Integer lowStockCount;
    private Integer outOfStockCount;

    @JdbiConstructor
    public DashboardStats(@ColumnName("label") @Nullable String label,
                          @ColumnName("revenue") @Nullable Integer revenue,
                          @ColumnName("totalRevenue") @Nullable Integer totalRevenue,
                          @ColumnName("lowStockCount") @Nullable Integer lowStockCount,
                          @ColumnName("outOfStockCount") @Nullable Integer outOfStockCount) {
        this.label = label;
        this.revenue = revenue;
        this.totalRevenue = totalRevenue;
        this.lowStockCount = lowStockCount;
        this.outOfStockCount = outOfStockCount;
    }

    public DashboardStats() {
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Integer getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Integer totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getLowStockCount() {
        return lowStockCount;
    }

    public void setLowStockCount(Integer lowStockCount) {
        this.lowStockCount = lowStockCount;
    }

    public Integer getOutOfStockCount() {
        return outOfStockCount;
    }

    public void setOutOfStockCount(Integer outOfStockCount) {
        this.outOfStockCount = outOfStockCount;
    }

    @Override
    public String toString() {
        return "DashboardStats{" +
                "month=" + label +
                ", revenue=" + revenue +
                ", totalRevenue=" + totalRevenue +
                ", lowStockCount=" + lowStockCount +
                ", outOfStockCount=" + outOfStockCount +
                '}';
    }
}