package hcmuaf.fit.mombabyecommerce.model;

import hcmuaf.fit.mombabyecommerce.contant.EPaymentMethod;
import hcmuaf.fit.mombabyecommerce.contant.OrderStatus;
import hcmuaf.fit.mombabyecommerce.contant.PaymentStatus;
import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.io.Serializable;
import java.time.LocalDate;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private LocalDate createAt;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    private Integer userId;
    private Integer cardId;
    private Integer addressId;
    private Boolean isCOD;

    private Integer quantity;
    private Integer total;
    private String productName;
    private String productImage;
    private String userName;

    private Integer shippingFee;
    private String shippingId;

    private Boolean isReviewed;
    private EPaymentMethod paymentMethod;
    private Integer voucher_id;
    private Integer discount_amount;

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

                 @ColumnName("paymentMethod") @Nullable EPaymentMethod paymentMethod,
                 @ColumnName("isReviewed") @Nullable Boolean isReviewed,
                 @ColumnName("voucher_id") @Nullable Integer voucher_id,
                 @ColumnName("discount_amount") @Nullable Integer discount_amount
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
        this.paymentMethod = paymentMethod;
        this.isReviewed = false;
        this.voucher_id = voucher_id;
        this.discount_amount = discount_amount;

    }
    public Integer getVoucher_id() {
        return voucher_id;
    }

    public void setVoucher_id(Integer voucher_id) {
        this.voucher_id = voucher_id;
    }

    public Integer getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(Integer discount_amount) {
        this.discount_amount = discount_amount;
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

    public EPaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(EPaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
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
//                ", isCOD=" + isCOD +
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
