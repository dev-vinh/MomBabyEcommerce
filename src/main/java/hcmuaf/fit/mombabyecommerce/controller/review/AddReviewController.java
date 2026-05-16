package hcmuaf.fit.mombabyecommerce.controller.review;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.ProductReviewService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@WebServlet(name = "AddReviewController", value = "/add-review")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 3 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class AddReviewController extends HttpServlet {
    private ProductReviewService productReviewService;
    private Cloudinary cloudinary;

    @Override
    public void init() throws ServletException {
        productReviewService = new ProductReviewService(DBConnection.getJdbi());
        cloudinary = initCloudinary();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject responseJson = new JSONObject();

        try {
            HttpSession session = request.getSession(false);
            Integer userId = session == null ? null : (Integer) session.getAttribute("userId");

            if (userId == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                responseJson.put("status", "error");
                responseJson.put("message", "Bạn cần đăng nhập để đánh giá sản phẩm.");
                response.getWriter().write(responseJson.toString());
                return;
            }

            int productId = parseInt(
                    request.getParameter("productId"),
                    "Thiếu mã sản phẩm."
            );

            int rating = parseInt(
                    request.getParameter("rating"),
                    "Vui lòng chọn số sao đánh giá."
            );

            String description = request.getParameter("description");

            List<String> imageUrls = uploadReviewImagesToCloudinary(request);

            productReviewService.saveOrUpdateReview(
                    userId,
                    productId,
                    rating,
                    description,
                    imageUrls
            );

            responseJson.put("status", "success");
            responseJson.put("message", "Cảm ơn bạn đã đánh giá sản phẩm.");

            response.getWriter().write(responseJson.toString());

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("status", "error");
            responseJson.put("message", e.getMessage());
            response.getWriter().write(responseJson.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseJson.put("status", "error");
            responseJson.put("message", "Có lỗi xảy ra khi gửi đánh giá.");
            response.getWriter().write(responseJson.toString());
        }
    }

    private Cloudinary initCloudinary() throws ServletException {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            String cloudName = dotenv.get("CLOUDINARY_CLOUD_NAME");
            String apiKey = dotenv.get("CLOUDINARY_API_KEY");
            String apiSecret = dotenv.get("CLOUDINARY_API_SECRET");

            if (isBlank(cloudName) || isBlank(apiKey) || isBlank(apiSecret)) {
                throw new ServletException("Thiếu cấu hình Cloudinary trong file .env");
            }

            return new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret,
                    "secure", true
            ));

        } catch (ServletException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException("Lỗi khi khởi tạo Cloudinary", e);
        }
    }

    private List<String> uploadReviewImagesToCloudinary(HttpServletRequest request)
            throws IOException, ServletException {

        Collection<Part> parts = request.getParts();
        List<Part> imageParts = new ArrayList<>();

        for (Part part : parts) {
            if ("images".equals(part.getName()) && part.getSize() > 0) {
                imageParts.add(part);
            }
        }

        if (imageParts.size() > 5) {
            throw new IllegalArgumentException("Chỉ được upload tối đa 5 ảnh.");
        }

        List<String> imageUrls = new ArrayList<>();

        if (imageParts.isEmpty()) {
            return imageUrls;
        }

        for (Part part : imageParts) {
            validateImage(part);

            try (InputStream inputStream = part.getInputStream()) {
                byte[] fileBytes = inputStream.readAllBytes();

                Map uploadResult = cloudinary.uploader().upload(
                        fileBytes,
                        ObjectUtils.asMap(
                                "folder", "mom-baby/reviews",
                                "resource_type", "image"
                        )
                );

                String imageUrl = (String) uploadResult.get("secure_url");

                if (imageUrl == null || imageUrl.isBlank()) {
                    imageUrl = (String) uploadResult.get("url");
                }

                if (imageUrl == null || imageUrl.isBlank()) {
                    throw new IllegalArgumentException("Upload ảnh lên Cloudinary thất bại.");
                }

                imageUrls.add(imageUrl);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Không thể upload ảnh đánh giá lên Cloudinary.");
            }
        }

        return imageUrls;
    }

    private void validateImage(Part part) {
        String contentType = part.getContentType();
        String fileName = part.getSubmittedFileName();

        if (contentType == null || fileName == null) {
            throw new IllegalArgumentException("File ảnh không hợp lệ.");
        }

        boolean validContentType =
                contentType.equals("image/jpeg")
                        || contentType.equals("image/png")
                        || contentType.equals("image/webp")
                        || contentType.equals("image/gif");

        if (!validContentType) {
            throw new IllegalArgumentException("Chỉ chấp nhận ảnh JPG, PNG, WEBP hoặc GIF.");
        }

        String lowerFileName = fileName.toLowerCase();

        boolean validExtension =
                lowerFileName.endsWith(".jpg")
                        || lowerFileName.endsWith(".jpeg")
                        || lowerFileName.endsWith(".png")
                        || lowerFileName.endsWith(".webp")
                        || lowerFileName.endsWith(".gif");

        if (!validExtension) {
            throw new IllegalArgumentException("Định dạng ảnh không hợp lệ.");
        }

        if (part.getSize() > 3 * 1024 * 1024) {
            throw new IllegalArgumentException("Mỗi ảnh tối đa 3MB.");
        }
    }

    private int parseInt(String value, String errorMessage) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}