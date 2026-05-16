package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.ProductReview;
import hcmuaf.fit.mombabyecommerce.model.ReviewStats;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(ProductReview.class)
public interface ProductReviewDao {

    @SqlQuery("""
            SELECT od.orderId
            FROM order_detail od
            JOIN orders o ON od.orderId = o.id
            WHERE o.userId = :userId
              AND od.productId = :productId
              AND o.orderStatus = 'DELIVERED'
            ORDER BY o.id DESC
            LIMIT 1
            """)
    Integer findDeliveredOrderId(@Bind("userId") int userId,
                                 @Bind("productId") int productId);

    @SqlQuery("""
            SELECT COUNT(*)
            FROM orders
            WHERE id = :orderId
              AND userId = :userId
              AND orderStatus = 'DELIVERED'
            """)
    int countDeliveredOrder(@Bind("orderId") int orderId,
                            @Bind("userId") int userId);

    @SqlQuery("""
            SELECT *
            FROM product_reviews
            WHERE userId = :userId
              AND productId = :productId
            LIMIT 1
            """)
    ProductReview findByUserAndProduct(@Bind("userId") int userId,
                                       @Bind("productId") int productId);

    @SqlUpdate("""
            INSERT INTO product_reviews
                (userId, productId, orderId, rating, description, isVerifiedPurchase)
            VALUES
                (:userId, :productId, :orderId, :rating, :description, 1)
            """)
    @GetGeneratedKeys
    int insertReview(@Bind("userId") Integer userId,
                     @Bind("productId") Integer productId,
                     @Bind("orderId") Integer orderId,
                     @Bind("rating") Integer rating,
                     @Bind("description") String description);

    @SqlUpdate("""
            UPDATE product_reviews
            SET rating = :rating,
                description = :description,
                updatedAt = CURRENT_TIMESTAMP
            WHERE id = :id
              AND userId = :userId
            """)
    int updateReview(@Bind("id") Integer id,
                     @Bind("userId") Integer userId,
                     @Bind("rating") Integer rating,
                     @Bind("description") String description);

    @SqlUpdate("""
            UPDATE order_detail
            SET isReviewed = 1
            WHERE orderId = :orderId
              AND productId = :productId
            """)
    void updateIsReviewed(@Bind("orderId") int orderId,
                          @Bind("productId") int productId);

    @SqlQuery("""
            SELECT
                pr.id,
                pr.userId,
                pr.productId,
                pr.orderId,
                pr.rating,
                pr.description,
                pr.isVerifiedPurchase,
                DATE_FORMAT(pr.createdAt, '%d/%m/%Y %H:%i') AS createdAt,
                DATE_FORMAT(pr.updatedAt, '%d/%m/%Y %H:%i') AS updatedAt,
                pr.adminReply,
                pr.repliedBy,
                DATE_FORMAT(pr.repliedAt, '%d/%m/%Y %H:%i') AS repliedAt,
                COALESCE(NULLIF(u.displayName, ''), u.fullName) AS userName,
                img.url AS avatarUrl,
                CAST(COUNT(DISTINCT l.userId) AS SIGNED) AS likeCount,
                CASE
                    WHEN :currentUserId IS NULL THEN 0
                    WHEN SUM(CASE WHEN l.userId = :currentUserId THEN 1 ELSE 0 END) > 0 THEN 1
                    ELSE 0
                END AS likedByCurrentUser
            FROM product_reviews pr
            JOIN users u ON pr.userId = u.id
            LEFT JOIN image img ON u.avatarId = img.id
            LEFT JOIN product_review_likes l ON pr.id = l.reviewId
            WHERE pr.productId = :productId
            GROUP BY pr.id, u.id, img.url
            ORDER BY
                CASE WHEN :sort = 'rating_desc' THEN pr.rating END DESC,
                CASE WHEN :sort = 'rating_asc' THEN pr.rating END ASC,
                CASE WHEN :sort = 'oldest' THEN pr.createdAt END ASC,
                pr.createdAt DESC
            LIMIT :limit OFFSET :offset
            """)
    List<ProductReview> getReviewsByProduct(@Bind("productId") int productId,
                                            @Bind("currentUserId") Integer currentUserId,
                                            @Bind("sort") String sort,
                                            @Bind("limit") int limit,
                                            @Bind("offset") int offset);

    @SqlQuery("""
            SELECT COUNT(*)
            FROM product_reviews
            WHERE productId = :productId
            """)
    int countReviewsByProduct(@Bind("productId") int productId);

    @SqlQuery("""
            SELECT
                COALESCE(ROUND(AVG(rating), 1), 0) AS averageRating,
                CAST(COUNT(*) AS SIGNED) AS totalReviews,
                CAST(COALESCE(SUM(CASE WHEN rating = 5 THEN 1 ELSE 0 END), 0) AS SIGNED) AS fiveStar,
                CAST(COALESCE(SUM(CASE WHEN rating = 4 THEN 1 ELSE 0 END), 0) AS SIGNED) AS fourStar,
                CAST(COALESCE(SUM(CASE WHEN rating = 3 THEN 1 ELSE 0 END), 0) AS SIGNED) AS threeStar,
                CAST(COALESCE(SUM(CASE WHEN rating = 2 THEN 1 ELSE 0 END), 0) AS SIGNED) AS twoStar,
                CAST(COALESCE(SUM(CASE WHEN rating = 1 THEN 1 ELSE 0 END), 0) AS SIGNED) AS oneStar
            FROM product_reviews
            WHERE productId = :productId
            """)
    @RegisterBeanMapper(ReviewStats.class)
    ReviewStats getReviewStats(@Bind("productId") int productId);

    @SqlUpdate("""
            INSERT INTO image (url)
            VALUES (:url)
            """)
    @GetGeneratedKeys
    int insertImage(@Bind("url") String url);

    @SqlUpdate("""
            INSERT INTO product_review_images (reviewId, imageId, sortOrder)
            VALUES (:reviewId, :imageId, :sortOrder)
            """)
    void insertReviewImage(@Bind("reviewId") int reviewId,
                           @Bind("imageId") int imageId,
                           @Bind("sortOrder") int sortOrder);

    @SqlUpdate("""
            DELETE FROM product_review_images
            WHERE reviewId = :reviewId
            """)
    void deleteReviewImages(@Bind("reviewId") int reviewId);

    @SqlQuery("""
            SELECT img.url
            FROM product_review_images pri
            JOIN image img ON pri.imageId = img.id
            WHERE pri.reviewId = :reviewId
            ORDER BY pri.sortOrder ASC, pri.id ASC
            """)
    List<String> getReviewImageUrls(@Bind("reviewId") int reviewId);

    @SqlQuery("""
            SELECT COUNT(*)
            FROM product_review_likes
            WHERE reviewId = :reviewId
              AND userId = :userId
            """)
    int countUserLike(@Bind("reviewId") int reviewId,
                      @Bind("userId") int userId);

    @SqlUpdate("""
            INSERT INTO product_review_likes (reviewId, userId)
            VALUES (:reviewId, :userId)
            """)
    void insertLike(@Bind("reviewId") int reviewId,
                    @Bind("userId") int userId);

    @SqlUpdate("""
            DELETE FROM product_review_likes
            WHERE reviewId = :reviewId
              AND userId = :userId
            """)
    void deleteLike(@Bind("reviewId") int reviewId,
                    @Bind("userId") int userId);

    @SqlQuery("""
            SELECT COUNT(*)
            FROM product_review_likes
            WHERE reviewId = :reviewId
            """)
    int countLikes(@Bind("reviewId") int reviewId);

    @SqlUpdate("""
            UPDATE product_reviews
            SET adminReply = :reply,
                repliedBy = :adminId,
                repliedAt = CURRENT_TIMESTAMP
            WHERE id = :reviewId
            """)
    int replyReview(@Bind("reviewId") int reviewId,
                    @Bind("adminId") int adminId,
                    @Bind("reply") String reply);
}