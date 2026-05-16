package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.ProductReviewDao;
import hcmuaf.fit.mombabyecommerce.model.ProductReview;
import hcmuaf.fit.mombabyecommerce.model.ReviewStats;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ProductReviewService {
    private final ProductReviewDao productReviewDao;

    public ProductReviewService(Jdbi jdbi) {
        this.productReviewDao = jdbi.onDemand(ProductReviewDao.class);
    }

    public boolean canReview(int userId, int productId) {
        return productReviewDao.findDeliveredOrderId(userId, productId) != null;
    }

    public ProductReview getMyReview(int userId, int productId) {
        ProductReview review = productReviewDao.findByUserAndProduct(userId, productId);

        if (review != null) {
            review.setImageUrls(productReviewDao.getReviewImageUrls(review.getId()));
        }

        return review;
    }

    public ReviewStats getReviewStats(int productId) {
        ReviewStats stats = productReviewDao.getReviewStats(productId);

        if (stats == null) {
            stats = new ReviewStats();
        }

        if (stats.getAverageRating() == null) stats.setAverageRating(0.0);
        if (stats.getTotalReviews() == null) stats.setTotalReviews(0);
        if (stats.getFiveStar() == null) stats.setFiveStar(0);
        if (stats.getFourStar() == null) stats.setFourStar(0);
        if (stats.getThreeStar() == null) stats.setThreeStar(0);
        if (stats.getTwoStar() == null) stats.setTwoStar(0);
        if (stats.getOneStar() == null) stats.setOneStar(0);

        return stats;
    }

    public int countReviewsByProduct(int productId) {
        return productReviewDao.countReviewsByProduct(productId);
    }

    public List<ProductReview> getReviewsByProduct(int productId, Integer currentUserId, String sort, int page, int size) {
        int offset = Math.max(page - 1, 0) * size;

        List<ProductReview> reviews = productReviewDao.getReviewsByProduct(
                productId,
                currentUserId,
                normalizeSort(sort),
                size,
                offset
        );

        for (ProductReview review : reviews) {
            review.setImageUrls(productReviewDao.getReviewImageUrls(review.getId()));
        }

        return reviews;
    }

    public void saveOrUpdateReview(int userId, int productId, int rating, String description, List<String> imageUrls) {
        validateReviewInput(rating, description);

        Integer deliveredOrderId = productReviewDao.findDeliveredOrderId(userId, productId);

        if (deliveredOrderId == null) {
            throw new IllegalArgumentException("Bạn chỉ có thể đánh giá sản phẩm đã mua và đơn đã hoàn thành.");
        }

        String cleanDescription = sanitize(description);

        ProductReview existingReview = productReviewDao.findByUserAndProduct(userId, productId);

        int reviewId;

        if (existingReview == null) {
            reviewId = productReviewDao.insertReview(
                    userId,
                    productId,
                    deliveredOrderId,
                    rating,
                    cleanDescription
            );
        } else {
            reviewId = existingReview.getId();

            productReviewDao.updateReview(
                    reviewId,
                    userId,
                    rating,
                    cleanDescription
            );
        }

        if (imageUrls != null && !imageUrls.isEmpty()) {
            productReviewDao.deleteReviewImages(reviewId);

            for (int i = 0; i < imageUrls.size(); i++) {
                int imageId = productReviewDao.insertImage(imageUrls.get(i));
                productReviewDao.insertReviewImage(reviewId, imageId, i);
            }
        }

        productReviewDao.updateIsReviewed(deliveredOrderId, productId);
    }

    public LikeResult toggleLike(int reviewId, int userId) {
        boolean alreadyLiked = productReviewDao.countUserLike(reviewId, userId) > 0;

        if (alreadyLiked) {
            productReviewDao.deleteLike(reviewId, userId);
        } else {
            productReviewDao.insertLike(reviewId, userId);
        }

        int likeCount = productReviewDao.countLikes(reviewId);

        return new LikeResult(!alreadyLiked, likeCount);
    }

    public boolean replyReview(int reviewId, int adminId, String reply) {
        String cleanReply = sanitize(reply);

        if (cleanReply == null || cleanReply.isBlank()) {
            throw new IllegalArgumentException("Nội dung phản hồi không được để trống.");
        }

        return productReviewDao.replyReview(reviewId, adminId, cleanReply) > 0;
    }

    private void validateReviewInput(int rating, String description) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Số sao phải từ 1 đến 5.");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Nội dung đánh giá không được để trống.");
        }

        if (description.trim().length() > 1000) {
            throw new IllegalArgumentException("Nội dung đánh giá tối đa 1000 ký tự.");
        }
    }

    private String sanitize(String input) {
        if (input == null) return null;

        return input.trim()
                .replaceAll("<[^>]*>", "")
                .replace("<", "")
                .replace(">", "");
    }

    private String normalizeSort(String sort) {
        if (sort == null) return "newest";

        return switch (sort) {
            case "rating_desc", "rating_asc", "oldest" -> sort;
            default -> "newest";
        };
    }

    public static class LikeResult {
        private final boolean liked;
        private final int likeCount;

        public LikeResult(boolean liked, int likeCount) {
            this.liked = liked;
            this.likeCount = likeCount;
        }

        public boolean isLiked() {
            return liked;
        }

        public int getLikeCount() {
            return likeCount;
        }
    }
}