package hcmuaf.fit.mombabyecommerce.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Integer id;
    private Integer cartId;
    private Integer productId;
    private Integer optionId;
    private Integer quantity;
    private Integer price;

    public CartItem() {
    }
    @JdbiConstructor
    public CartItem(
            @ColumnName("id") Integer id,
            @ColumnName("cartId") Integer cartId,
            @ColumnName("productId") Integer productId,
            @ColumnName("optionId") Integer optionId,
            @ColumnName("quantity") Integer quantity,
            @ColumnName("price") Integer price
    ) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
