package hcmuaf.fit.mombabyecommerce.controller.product;


import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.*;
import hcmuaf.fit.mombabyecommerce.service.ImageService;
import hcmuaf.fit.mombabyecommerce.service.OptionService;
import hcmuaf.fit.mombabyecommerce.service.ProductReviewService;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@WebServlet(name = "ProductDetailController", value = "/product-detail")
public class ProductDetailController extends HttpServlet {
    ProductService productService = new ProductService(DBConnection.getJdbi());
    ImageService imageService = new ImageService(DBConnection.getJdbi());
    OptionService optionService = new OptionService(DBConnection.getJdbi());
    ProductReviewService productReviewService = new ProductReviewService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int productId = Integer.parseInt(request.getParameter("id"));
        Product product = productService.getProductById(productId);

        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }

        Integer productPrice = productService.getMinimumPriceForProduct(productId);

        if (product.getOptionId() != null) {
            productPrice = productService.getPriceForOption(product.getOptionId());
        }

        List<String> images = imageService.getAllImagesByProductId(product.getId());
        String primaryImageUrl = imageService.getImageUrlById(product.getImageId());

        List<String> descriptions = new ArrayList<>();

        if (product.getDescription() != null) {
            descriptions = List.of(product.getDescription().split("\\n"));
        }

        List<OptionVariant> optionVariant =
                optionService.getOptionDetailsByProductId(product.getId());

        Map<Integer, ProductOptionDTO> optionMap = new LinkedHashMap<>();

        for (OptionVariant op : optionVariant) {
            ProductOptionDTO dto = optionMap.get(op.getId());

            if (dto == null) {
                dto = new ProductOptionDTO(
                        op.getId(),
                        op.getProductId(),
                        op.getPrice(),
                        op.getStock()
                );

                dto.setVariantText("");
                optionMap.put(op.getId(), dto);
            }

            if (op.getVariantName() != null && op.getVariantValue() != null) {
                String text = op.getVariantName() + ": " + op.getVariantValue();

                if (dto.getVariantText() == null || dto.getVariantText().isEmpty()) {
                    dto.setVariantText(text);
                } else {
                    dto.setVariantText(dto.getVariantText() + " | " + text);
                }
            }
        }

        List<ProductOptionDTO> productOptions = new ArrayList<>(optionMap.values());

        for (ProductOptionDTO dto : productOptions) {
            if (dto.getVariantText() == null || dto.getVariantText().isEmpty()) {
                dto.setVariantText("Mặc định");
            }
        }

        HttpSession session = request.getSession(false);
        Integer currentUserId = session == null ? null : (Integer) session.getAttribute("userId");

        String sort = request.getParameter("sort");
        int page = parsePage(request.getParameter("reviewPage"));
        int size = 5;

        ReviewStats reviewStats = productReviewService.getReviewStats(productId);
        int totalReviews = productReviewService.countReviewsByProduct(productId);
        int totalPages = (int) Math.ceil(totalReviews * 1.0 / size);

        List<ProductReview> reviews =
                productReviewService.getReviewsByProduct(productId, currentUserId, sort, page, size);

        ProductReview myReview =
                currentUserId == null ? null : productReviewService.getMyReview(currentUserId, productId);

        boolean canReview =
                currentUserId != null && productReviewService.canReview(currentUserId, productId);

        request.setAttribute("images", images);
        request.setAttribute("primaryImageUrl", primaryImageUrl);
        request.setAttribute("product", product);
        request.setAttribute("descriptions", descriptions);
        request.setAttribute("productPrice", productPrice);
        request.setAttribute("productOptions", productOptions);

        request.setAttribute("reviewStats", reviewStats);
        request.setAttribute("reviews", reviews);
        request.setAttribute("myReview", myReview);
        request.setAttribute("canReview", canReview);
        request.setAttribute("reviewSort", sort == null ? "newest" : sort);
        request.setAttribute("reviewPage", page);
        request.setAttribute("reviewTotalPages", totalPages);

        productService.increaseNoOfViews(productId);

        request.getRequestDispatcher("product_detail/product-detail.jsp")
                .forward(request, response);
    }

    private int parsePage(String pageParam) {
        try {
            int page = Integer.parseInt(pageParam);
            return Math.max(page, 1);
        } catch (Exception e) {
            return 1;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}

