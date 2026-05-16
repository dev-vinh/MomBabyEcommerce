package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.ProductReviewDao;
import hcmuaf.fit.mombabyecommerce.model.ProductReview;
import org.jdbi.v3.core.Jdbi;

public class ReviewService {
    private final ProductReviewDao reviewDao;

    public ReviewService(Jdbi jdbi) {
        this.reviewDao = jdbi.onDemand(ProductReviewDao.class);
    }

    public boolean isOrderDelivered(int orderId, int userId) {
        return reviewDao.countDeliveredOrder(orderId, userId) > 0;
    }

    public ProductReview getReview(int userId, int orderId, int productId) {
        return reviewDao.findByUserAndProduct(userId, productId);
    }

    public Boolean addReview(ProductReview review) {
        if (review == null) {
            return false;
        }

        Integer deliveredOrderId = reviewDao.findDeliveredOrderId(
                review.getUserId(),
                review.getProductId()
        );

        if (deliveredOrderId == null) {
            return false;
        }

        ProductReview existingReview = reviewDao.findByUserAndProduct(
                review.getUserId(),
                review.getProductId()
        );

        if (existingReview != null) {
            reviewDao.updateReview(
                    existingReview.getId(),
                    review.getUserId(),
                    review.getRating(),
                    review.getDescription()
            );

            reviewDao.updateIsReviewed(deliveredOrderId, review.getProductId());
            return true;
        }

        int reviewId = reviewDao.insertReview(
                review.getUserId(),
                review.getProductId(),
                deliveredOrderId,
                review.getRating(),
                review.getDescription()
        );

        reviewDao.updateIsReviewed(deliveredOrderId, review.getProductId());

        return reviewId > 0;
    }
}