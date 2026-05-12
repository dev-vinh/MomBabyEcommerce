package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.ProductReviewDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.contant.OrderStatus;
import hcmuaf.fit.mombabyecommerce.model.ProductReview;
import org.jdbi.v3.core.Jdbi;

public class ProductReviewService {
    Jdbi jdbi;
    ProductReviewDao productReviewDao;

    public ProductReviewService(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.productReviewDao = jdbi.onDemand(ProductReviewDao.class);
    }

    public boolean isOrderDelivered(int orderId, int userId) {
        OrderStatus orderStatus = productReviewDao.getOrderStatus(orderId, userId);
        return orderStatus != null && orderStatus == OrderStatus.DELIVERY;
    }

    public ProductReview getReview(int userId, int orderId, int productId) {
        return productReviewDao.getReview(userId, orderId, productId);
    }


    public Boolean addReview(ProductReview review) {
        int existing = productReviewDao.countExistingReview(
                review.getUserId(),
                review.getOrderId(),
                review.getProductId()
        );

        if (existing > 0) {
            System.out.println(" Review đã tồn tại");
            return false;
        }

        boolean success = productReviewDao.addReview(
                review.getUserId(),
                review.getProductId(),
                review.getOrderId(),
                review.getRating(),
                review.getDescription()
        );

        if (success) {
            productReviewDao.updateIsReviewed(review.getOrderId(), review.getProductId());
        }

        return success;


    }
    public static void main(String[] args) {
        ReviewService reviewService = new ReviewService(DBConnection.getJdbi());
        ProductReview review = new ProductReview();
        review.setDescription("Test Review");
        review.setOrderId(57);
        review.setRating(5);
        review.setUserId(39);
        review.setProductId(44);

        System.out.println(reviewService.addReview(review));
    }
}
