package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.CartDB;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterConstructorMapper(CartDB.class)
public interface CartDao {
    @SqlQuery("""
        SELECT *
        FROM cart
        WHERE userId = :userId
        AND status = 'ACTIVE'
        LIMIT 1
    """)
    CartDB getActiveCartByUserId(@Bind("userId") Integer userId);

    @SqlQuery("""
        SELECT *
        FROM cart
        WHERE id = :cartId
    """)
    CartDB getCartById(@Bind("cartId") Integer cartId);

    @SqlQuery("""
        SELECT *
        FROM cart
        WHERE sessionId = :sessionId
        AND status = 'ACTIVE'
        LIMIT 1
    """)
    CartDB getCartBySessionId(@Bind("sessionId") String sessionId);

    @SqlUpdate("""
        INSERT INTO cart(userId, sessionId, status)
        VALUES (:userId, :sessionId, 'ACTIVE')
    """)
    int createCart(
            @Bind("userId") Integer userId,
            @Bind("sessionId") String sessionId
    );

    @SqlUpdate("""
        UPDATE cart
        SET status = :status
        WHERE id = :cartId
    """)
    int updateStatus(
            @Bind("cartId") Integer cartId,
            @Bind("status") String status
    );

    @SqlUpdate("""
        DELETE FROM cart
        WHERE id = :cartId
    """)
    int deleteCart(@Bind("cartId") Integer cartId);
}
