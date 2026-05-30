package hcmuaf.fit.mombabyecommerce.model;

import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.time.LocalDate;

public class DashboardOrder {
    private Integer id;
    private LocalDate createAt;
    private String paymentStatus;
    private String orderStatus;
    private Integer userId;
    private Integer addressId;
    private Integer cardId;
    private Boolean isCOD;
    private Integer total;
    private String userName;
    private Integer shippingFee;

    @JdbiConstructor
    public DashboardOrder(@ColumnName("id") Integer id,
                          @ColumnName("createAt") @Nullable LocalDate createAt,
                          @ColumnName("paymentStatus") @Nullable String paymentStatus,
                          @ColumnName("orderStatus") @Nullable String orderStatus,
                          @ColumnName("userId") @Nullable Integer userId,
                          @ColumnName("addressId") @Nullable Integer addressId,
                          @ColumnName("cardId") @Nullable Integer cardId,
                          @ColumnName("isCOD") @Nullable Boolean isCOD,
                          @ColumnName("total") @Nullable Integer total,
                          @ColumnName("userName") @Nullable String userName,
                          @ColumnName("shippingFee") @Nullable Integer shippingFee) {
        this.id = id;
        this.createAt = createAt;
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
        this.userId = userId;
        this.addressId = addressId;
        this.cardId = cardId;
        this.isCOD = isCOD;
        this.total = total;
        this.userName = userName;
        this.shippingFee = shippingFee;
    }

    public DashboardOrder() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Boolean getIsCOD() {
        return isCOD;
    }

    public void setIsCOD(Boolean isCOD) {
        this.isCOD = isCOD;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Integer shippingFee) {
        this.shippingFee = shippingFee;
    }
}