package hcmuaf.fit.mombabyecommerce.model;


import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private Integer total;
    private Integer optionId;
    private String productName;
    private String imageUrl;

    @JdbiConstructor
    public OrderDetail(@ColumnName("id") @Nullable Integer id,
                       @ColumnName("orderId") @Nullable Integer orderId,
                       @ColumnName("productId") @Nullable Integer productId,
                       @ColumnName("quantity") @Nullable Integer quantity,
                       @ColumnName("total") @Nullable Integer total,
                       @ColumnName("optionId") @Nullable Integer optionId,
                       @ColumnName("productName") @Nullable String productName,
                       @ColumnName("imageUrl") @Nullable String imageUrl
    ) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.total = total;
        this.optionId = optionId;
        this.productName = productName;
        this.imageUrl = imageUrl;
    }

    public OrderDetail() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", total=" + total +
                ", optionId=" + optionId +
                ", productName='" + productName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

