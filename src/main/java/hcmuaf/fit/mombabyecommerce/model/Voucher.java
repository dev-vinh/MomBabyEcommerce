package hcmuaf.fit.mombabyecommerce.model;

import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Voucher implements Serializable {

    private Integer id;

    private String code;

    private Integer discountPercent;

    private Double minOrderAmount;

    private Double maxDiscount;

    private Integer quantity;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean active;

    public Voucher() {
    }

    @JdbiConstructor
    public Voucher(
            @ColumnName("id")
            @Nullable Integer id,

            @ColumnName("code")
            @Nullable String code,

            @ColumnName("discount_percent")
            @Nullable Integer discountPercent,

            @ColumnName("min_order_amount")
            @Nullable Double minOrderAmount,

            @ColumnName("max_discount")
            @Nullable Double maxDiscount,

            @ColumnName("quantity")
            @Nullable Integer quantity,
            @ColumnName("start_date")
            @Nullable LocalDateTime startDate,
            @ColumnName("end_date")
            @Nullable LocalDateTime endDate,
            @ColumnName("active")
            @Nullable Boolean active
    ) {
        this.id = id;
        this.code = code;
        this.discountPercent = discountPercent;
        this.minOrderAmount = minOrderAmount;
        this.maxDiscount = maxDiscount;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Double getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(Double minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public Double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(Double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
