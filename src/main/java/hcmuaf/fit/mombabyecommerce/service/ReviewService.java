package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.ProductReviewDao;
import hcmuaf.fit.mombabyecommerce.contant.OrderStatus;
import hcmuaf.fit.mombabyecommerce.model.ProductReview;
import org.jdbi.v3.core.Jdbi;

public class ReviewService {
    Jdbi jdbi;
    ProductReviewDao reviewDao;

    public ReviewService(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.reviewDao = jdbi.onDemand(ProductReviewDao.class);
    }
    public boolean isOrderDelivered(int orderId, int userId) {
        OrderStatus orderStatus = reviewDao.getOrderStatus(orderId, userId);
        return orderStatus != null && orderStatus == OrderStatus.DELIVERY;
    }

    public ProductReview getReview(int userId, int orderId, int productId) {
        return reviewDao.getReview(userId, orderId, productId);
    }

    public Boolean addReview(ProductReview review) {
        int existing = reviewDao.countExistingReview(
                review.getUserId(),
                review.getOrderId(),
                review.getProductId()
        );

        if (existing > 0) {
            System.out.println(" ProductReview đã tồn tại");
            return false;
        }

        boolean success = reviewDao.addReview(
                review.getUserId(),
                review.getProductId(),
                review.getOrderId(),
                review.getRating(),
                review.getDescription()
        );

        if (success) {
            reviewDao.updateIsReviewed(review.getOrderId(), review.getProductId());
        }
        return success;
    }
}
