package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Image;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterConstructorMapper(Image.class)
public interface ImageDao {

    @SqlUpdate("INSERT INTO image (url) VALUES (:url)")
    @GetGeneratedKeys
    int saveImage(@Bind("url") String url);

    @SqlUpdate("INSERT INTO product_images (productId, imageId) VALUES (:productId, :imageId)")
    boolean addImageToProduct(@Bind("productId") Integer productId,
                              @Bind("imageId") Integer imageId);

    @SqlUpdate("DELETE FROM product_images WHERE productId = :productId")
    int deleteImagesByProductId(@Bind("productId") Integer productId);

    @SqlQuery("SELECT url FROM image WHERE id = :id")
    String getImageUrlById(@Bind("id") int id);

    @SqlQuery("""
            SELECT image.url
            FROM image
            INNER JOIN product_images ON image.id = product_images.imageId
            WHERE product_images.productId = :productId
            ORDER BY product_images.id ASC
            """)
    List<String> getAllImagesByProductId(@Bind("productId") Integer productId);

    @SqlQuery("""
            SELECT image.id as id,
                   image.url as url
            FROM image
            INNER JOIN product_images ON image.id = product_images.imageId
            WHERE product_images.productId = :productId
            ORDER BY product_images.id ASC
            """)
    List<Image> getImagesByProductId(@Bind("productId") Integer productId);
}
