package hcmuaf.fit.mombabyecommerce.controller.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.Dao.ImageDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Image;
import hcmuaf.fit.mombabyecommerce.util.ResponseWrapper;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@WebServlet("/api/uploadImage")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 3 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class UploadImageController extends HttpServlet {
    private Cloudinary cloudinary;
    private ImageDao imageDao;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        cloudinary = initCloudinary();
        imageDao = DBConnection.getJdbi().onDemand(ImageDao.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Part> imageParts = getImageParts(request);

            if (imageParts.isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy file để upload.");
            }

            if (imageParts.size() > 10) {
                throw new IllegalArgumentException("Chỉ được upload tối đa 10 ảnh sản phẩm mỗi lần.");
            }

            List<Image> uploadedImages = new ArrayList<>();

            for (Part part : imageParts) {
                validateImage(part);

                try (InputStream inputStream = part.getInputStream()) {
                    byte[] fileBytes = inputStream.readAllBytes();

                    Map uploadResult = cloudinary.uploader().upload(
                            fileBytes,
                            ObjectUtils.asMap(
                                    "folder", "mom-baby/products",
                                    "resource_type", "image"
                            )
                    );

                    String imageUrl = (String) uploadResult.get("secure_url");
                    if (imageUrl == null || imageUrl.isBlank()) {
                        imageUrl = (String) uploadResult.get("url");
                    }
                    if (imageUrl == null || imageUrl.isBlank()) {
                        throw new IllegalArgumentException("Upload ảnh sản phẩm lên Cloudinary thất bại.");
                    }

                    int generatedId = imageDao.saveImage(imageUrl);
                    uploadedImages.add(new Image(generatedId, imageUrl));
                }
            }

            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_OK,
                    "success",
                    "Upload ảnh sản phẩm thành công.",
                    uploadedImages
            ));
        } catch (IllegalArgumentException e) {
            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "error",
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            e.printStackTrace();
            writeResponse(response, new ResponseWrapper<>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "error",
                    "Không thể upload ảnh sản phẩm lên Cloudinary.",
                    null
            ));
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

    private List<Part> getImageParts(HttpServletRequest request) throws IOException, ServletException {
        Collection<Part> parts = request.getParts();
        List<Part> imageParts = new ArrayList<>();

        for (Part part : parts) {
            if (part.getSize() > 0 && part.getSubmittedFileName() != null) {
                imageParts.add(part);
            }
        }

        return imageParts;
    }

    private void validateImage(Part part) {
        String contentType = part.getContentType();
        String fileName = part.getSubmittedFileName();

        if (contentType == null || fileName == null) {
            throw new IllegalArgumentException("File ảnh không hợp lệ.");
        }

        boolean validContentType = contentType.equals("image/jpeg")
                || contentType.equals("image/png")
                || contentType.equals("image/webp")
                || contentType.equals("image/gif");

        if (!validContentType) {
            throw new IllegalArgumentException("Chỉ chấp nhận ảnh JPG, PNG, WEBP hoặc GIF.");
        }

        String lowerFileName = fileName.toLowerCase();
        boolean validExtension = lowerFileName.endsWith(".jpg")
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

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void writeResponse(HttpServletResponse response, ResponseWrapper<?> responseWrapper) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(responseWrapper.getStatusCode());
        response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
    }
}
