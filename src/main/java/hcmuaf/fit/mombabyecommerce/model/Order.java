package hcmuaf.fit.mombabyecommerce.model;

import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;
import vn.edu.hcmuaf.fit.trang_web_ban_hang_san_pham_cho_me_va_be.contant.OrderStatus;
import vn.edu.hcmuaf.fit.trang_web_ban_hang_san_pham_cho_me_va_be.contant.PaymentStatus;

import java.time.LocalDate;

public class Order {
    Integer id;
    LocalDate createAt;
    PaymentStatus paymentStatus;
    OrderStatus orderStatus;
    Integer userId;
    Integer cardId;
    Integer addressId;
    Boolean isCOD;


    // Su dung cho đại diẹn order
    Integer quantity;
    Integer total;
    String productName;
    String productImage;
    String userName;

    //    Vận chuyển
    Integer shippingFee;
    String shippingId;

    //    Review
    Boolean isReviewed;


    @JdbiConstructor
    public Order(@ColumnName("id") @Nullable Integer id,
                 @ColumnName("createAt") @Nullable LocalDate createAt,
                 @ColumnName("paymentStatus") @Nullable PaymentStatus paymentStatus,
                 @ColumnName("orderStatus") @Nullable OrderStatus orderStatus,
                 @ColumnName("userId") @Nullable Integer userId,
                 @ColumnName("cardId") @Nullable Integer cardId,
                 @ColumnName("addressId") @Nullable Integer addressId,
                 @ColumnName("isCOD") @Nullable Boolean isCOD,

                 @ColumnName("quantity") @Nullable Integer quantity,
                 @ColumnName("total") @Nullable Integer total,
                 @ColumnName("productName") @Nullable String productName,
                 @ColumnName("productImage") @Nullable String productImage,
                 @ColumnName("userName") @Nullable String userName,


                 @ColumnName("shippingFee") @Nullable Integer shippingFee,
                 @ColumnName("shippingId") @Nullable String shippingId,


                 @ColumnName("isReviewed") @Nullable Boolean isReviewed

    ){
        this.id = id;
        this.createAt = createAt;
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
        this.userId = userId;
        this.cardId = cardId;
        this.addressId = addressId;
        this.isCOD = isCOD;
        this.quantity = quantity;
        this.total = total;
        this.productName = productName;
        this.productImage = productImage;
        this.userName = userName;
        this.shippingFee = shippingFee;
        this.shippingId = shippingId;
        this.isReviewed = false;

    }

    public Order( ) {
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

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Boolean getCOD() {
        return isCOD;
    }

    public void setCOD(Boolean COD) {
        isCOD = COD;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
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

    public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public Boolean getReviewed() {
        return isReviewed;
    }

    public void setReviewed(Boolean reviewed) {
        isReviewed = reviewed;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", createAt=" + createAt +
                ", paymentStatus=" + paymentStatus +
                ", orderStatus=" + orderStatus +
                ", userId=" + userId +
                ", cardId=" + cardId +
                ", addressId=" + addressId +
                ", isCOD=" + isCOD +
                ", quantity=" + quantity +
                ", total=" + total +
                ", productName='" + productName + '\'' +
                ", productImage='" + productImage + '\'' +
                ", userName='" + userName + '\'' +
                ", shippingFee=" + shippingFee +
                ", shippingId='" + shippingId + '\'' +
                ", isReviewed=" + isReviewed +
                '}';
    }
}
