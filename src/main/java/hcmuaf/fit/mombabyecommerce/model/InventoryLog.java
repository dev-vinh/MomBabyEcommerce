package hcmuaf.fit.mombabyecommerce.model;

import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.io.Serializable;

public class InventoryLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer optionVariantId;
    private Integer productId;
    private String actionType;
    private Integer quantityChange;
    private Integer stockBefore;
    private Integer stockAfter;
    private String reason;
    private Integer userId;
    private String userName;
    private String createdAt;
    private String productName;
    private String variantLabel;

    public InventoryLog() {}

    @JdbiConstructor
    public InventoryLog(@ColumnName("id") @Nullable Integer id,
                       @ColumnName("option_variant_id") @Nullable Integer optionVariantId,
                       @ColumnName("product_id") @Nullable Integer productId,
                       @ColumnName("action_type") @Nullable String actionType,
                       @ColumnName("quantity_change") @Nullable Integer quantityChange,
                       @ColumnName("stock_before") @Nullable Integer stockBefore,
                       @ColumnName("stock_after") @Nullable Integer stockAfter,
                       @ColumnName("reason") @Nullable String reason,
                       @ColumnName("user_id") @Nullable Integer userId,
                       @ColumnName("user_name") @Nullable String userName,
                       @ColumnName("created_at") @Nullable String createdAt,
                       @ColumnName("product_name") @Nullable String productName,
                       @ColumnName("variant_label") @Nullable String variantLabel) {
        this.id = id;
        this.optionVariantId = optionVariantId;
        this.productId = productId;
        this.actionType = actionType;
        this.quantityChange = quantityChange;
        this.stockBefore = stockBefore;
        this.stockAfter = stockAfter;
        this.reason = reason;
        this.userId = userId;
        this.userName = userName;
        this.createdAt = createdAt;
        this.productName = productName;
        this.variantLabel = variantLabel;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getOptionVariantId() { return optionVariantId; }
    public void setOptionVariantId(Integer optionVariantId) { this.optionVariantId = optionVariantId; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public Integer getQuantityChange() { return quantityChange; }
    public void setQuantityChange(Integer quantityChange) { this.quantityChange = quantityChange; }

    public Integer getStockBefore() { return stockBefore; }
    public void setStockBefore(Integer stockBefore) { this.stockBefore = stockBefore; }

    public Integer getStockAfter() { return stockAfter; }
    public void setStockAfter(Integer stockAfter) { this.stockAfter = stockAfter; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getVariantLabel() { return variantLabel; }
    public void setVariantLabel(String variantLabel) { this.variantLabel = variantLabel; }
}
