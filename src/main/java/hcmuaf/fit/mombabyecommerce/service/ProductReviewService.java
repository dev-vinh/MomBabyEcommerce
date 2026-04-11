package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.ProductReviewDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.ProductReview;
import org.jdbi.v3.core.Jdbi;

public class ProductReviewService {
    Jdbi jdbi;
    ProductReviewDao productReviewDao;

    public ProductReviewService(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.productReviewDao = jdbi.onDemand(ProductReviewDao.class);
    }

    public Boolean addReview(ProductReview review) {
        return productReviewDao.addReview(review.getUserId(),
                review.getProductId(),
                review.getOrderId(),
                review.getRating(),
                review.getDescription()
        );
    }

    public static void main(String[] args) {
        ProductReviewService reviewService = new ProductReviewService(DBConnection.getJdbi());
//        ProductReview review = new ProductReview();
//        review.setDescription("Test ProductReview");
//        review.setOrderId(57);
//        review.setRating(5);
//        review.setUserId(39);
//        review.setProductId(44);

//        System.out.println(reviewService.addReview(review));
    }
}
