package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.contant.OrderStatus;
import hcmuaf.fit.mombabyecommerce.model.ProductReview;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
@RegisterConstructorMapper(ProductReview.class)
public interface ProductReviewDao {
    @SqlUpdate(value =
            "INSERT INTO product_reviews (userId, productId, orderid, rating, description) " +
                    "VALUES (:userId, :productId, :orderId, :rating, :description)")

    Boolean addReview(@Bind("userId") Integer userId,
                      @Bind("productId") Integer productId,
                      @Bind("orderId") Integer orderId,
                      @Bind("rating") Integer rating,
                      @Bind("description") String description

    );

    @SqlUpdate("UPDATE order_detail SET isReviewed = 1 WHERE orderId = :orderId AND productId = :productId")
    void updateIsReviewed(@Bind("orderId") int orderId,
                          @Bind("productId") int productId);


    @SqlQuery("SELECT orderStatus FROM orders WHERE id = :orderId AND userId = :userId")
    OrderStatus getOrderStatus(@Bind("orderId") int orderId, @Bind("userId") int userId);



    @SqlQuery("SELECT * FROM product_reviews WHERE userId = :userId AND orderId = :orderId AND productId = :productId LIMIT 1")
    ProductReview getReview(@Bind("userId") int userId,
                            @Bind("orderId") int orderId,
                            @Bind("productId") int productId);


    @SqlQuery("SELECT COUNT(*) FROM product_reviews WHERE userId = :userId AND orderId = :orderId AND productId = :productId")
    int countExistingReview(@Bind("userId") int userId,
                            @Bind("orderId") int orderId,
                            @Bind("productId") int productId);
}
