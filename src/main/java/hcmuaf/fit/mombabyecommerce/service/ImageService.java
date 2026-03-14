package hcmuaf.fit.mombabyecommerce.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import hcmuaf.fit.mombabyecommerce.Dao.ImageDao;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Map;
public class ImageService {
    Jdbi jdbi;
    private  Cloudinary cloudinary;

    private ImageDao imageDao;

    public ImageService(Jdbi jdbi, Cloudinary cloudinary, ImageDao imageDao) {
        this.jdbi = jdbi;
        this.cloudinary = cloudinary;
        this.imageDao = imageDao;
    }


    public ImageService(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.imageDao = jdbi.onDemand(ImageDao.class);
    }


    public ImageService(Cloudinary cloudinary, ImageDao imageDao) {
        this.cloudinary = cloudinary;
        this.imageDao = imageDao;
    }

    // Upload ảnh lên Cloudinary và lưu vào DB
    public String uploadImage(byte[] fileBytes) throws Exception {
        Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.emptyMap());
        String imageUrl = (String) uploadResult.get("url");
        imageDao.saveImage(imageUrl);
        return imageUrl;
    }

    public boolean addImageToProduct(Integer productId, Integer imageId) {
        return imageDao.addImageToProduct(productId, imageId);
    }

    public int saveImage(String imageUrl) {
        return imageDao.saveImage(imageUrl);
    }

    public String getImageUrlById(int imageId) {
        return imageDao.getImageUrlById(imageId);
    }


    public List<String> getAllImagesByProductId(Integer productId) {
        return imageDao.getAllImagesByProductId(productId);
    }

}
