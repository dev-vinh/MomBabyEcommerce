package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.CartItem;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterConstructorMapper(CartItem.class)
public interface CartItemDao {
    @SqlQuery("""
        SELECT *
        FROM cart_item
        WHERE cartId = :cartId
    """)
    List<CartItem> getItemsByCartId(@Bind("cartId") Integer cartId);

    @SqlQuery("""
        SELECT *
        FROM cart_item
        WHERE cartId = :cartId
        AND productId = :productId
        AND optionId = :optionId
    """)
    CartItem getItem(
            @Bind("cartId") Integer cartId,
            @Bind("productId") Integer productId,
            @Bind("optionId") Integer optionId
    );

    @SqlUpdate("""
        INSERT INTO cart_item(cartId, productId, optionId, quantity, price)
        VALUES (:cartId, :productId, :optionId, :quantity, :price)
        ON DUPLICATE KEY UPDATE
            quantity = quantity + :quantity
    """)
    int addItem(
            @Bind("cartId") Integer cartId,
            @Bind("productId") Integer productId,
            @Bind("optionId") Integer optionId,
            @Bind("quantity") Integer quantity,
            @Bind("price") Integer price
    );

    @SqlUpdate("""
        UPDATE cart_item
        SET quantity = :quantity
        WHERE cartId = :cartId
        AND productId = :productId
        AND optionId = :optionId
    """)
    int updateQuantity(
            @Bind("cartId") Integer cartId,
            @Bind("productId") Integer productId,
            @Bind("optionId") Integer optionId,
            @Bind("quantity") Integer quantity
    );

    @SqlUpdate("""
        DELETE FROM cart_item
        WHERE cartId = :cartId
        AND productId = :productId
        AND optionId = :optionId
    """)
    int deleteItem(
            @Bind("cartId") Integer cartId,
            @Bind("productId") Integer productId,
            @Bind("optionId") Integer optionId
    );

    @SqlUpdate("""
        DELETE FROM cart_item
        WHERE cartId = :cartId
    """)
    int clearCart(@Bind("cartId") Integer cartId);
}
