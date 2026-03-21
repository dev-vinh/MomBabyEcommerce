package hcmuaf.fit.mombabyecommerce.controller.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hcmuaf.fit.mombabyecommerce.Dao.ImageDao;
import hcmuaf.fit.mombabyecommerce.model.Image;
import hcmuaf.fit.mombabyecommerce.service.ImageService;
import hcmuaf.fit.mombabyecommerce.util.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@WebServlet("/api/uploadImage")
public class UploadImageController extends HttpServlet {
    private ImageService imageService;

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties properties = new Properties();
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
                if (input == null) {
                    throw new ServletException("Không tìm thấy file application.properties");
                }
                properties.load(input);
            }

            String dbUrl = properties.getProperty("db.url");
            String dbUsername = properties.getProperty("db.username");
            String dbPassword = properties.getProperty("db.password");

            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dqvztmq3e",
                    "api_key", "762487298112211",
                    "api_secret", "YXeS3WUg2HJ-1Pk1tfoePQotfXk"
            ));

            Jdbi jdbi = Jdbi.create(dbUrl, dbUsername, dbPassword);
            jdbi.installPlugin(new SqlObjectPlugin());

            ImageDao imageDao = jdbi.onDemand(ImageDao.class);
            imageService = new ImageService(cloudinary, imageDao);

        } catch (ClassNotFoundException e) {
            throw new ServletException("MySQL  Driver không tìm thấy", e);
        } catch (Exception e) {
            throw new ServletException("Lỗi khi khởi tạo", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        Collection<Part> fileParts = request.getParts();
        List<Image> uploadedImages = new ArrayList<>();

        if (fileParts != null && !fileParts.isEmpty()) {
            for (Part filePart : fileParts) {
                String fileName = filePart.getSubmittedFileName();
                String contentType = filePart.getContentType();
                if (contentType != null && fileName != null &&
                        (contentType.equals("image/png") || contentType.equals("image/jpeg")) &&
                        (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))) {

                    if (filePart.getSize() > 0) {
                        try (InputStream inputStream = filePart.getInputStream()) {
                            byte[] fileBytes = inputStream.readAllBytes();
                            String imageUrl = imageService.uploadImage(fileBytes);
                            int generatedId = imageService.saveImage(imageUrl);
                            uploadedImages.add(new Image(generatedId, imageUrl));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("File không hợp lệ: " + fileName + " (Loại: " + contentType + ")");
                }
            }

            if (!uploadedImages.isEmpty()) {
                ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(
                        HttpServletResponse.SC_OK,
                        "Thành công",
                        "Tất cả ảnh đã được upload thành công!",
                        uploadedImages
                );
                response.setStatus(HttpServletResponse.SC_OK); // 200
                response.getWriter().println(objectMapper.writeValueAsString(responseWrapper));
            } else {
                ResponseWrapper<Object> errorResponse = new ResponseWrapper<>(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Lỗi",
                        "Chỉ chấp nhận ảnh PNG hoặc JPG hợp lệ để upload.",
                        null
                );
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                response.getWriter().println(objectMapper.writeValueAsString(errorResponse));
            }
        } else {
            ResponseWrapper<Object> errorResponse = new ResponseWrapper<>(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Lỗi",
                    "Không tìm thấy file để upload.",
                    null
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            response.getWriter().println(objectMapper.writeValueAsString(errorResponse));
        }
    }



}
