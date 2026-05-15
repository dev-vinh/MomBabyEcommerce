package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.model.Variant;
import jakarta.annotation.Nullable;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterConstructorMapper(Product.class)
public interface ProductDao {

    @SqlQuery("""
            SELECT p.id, p.name, p.description, p.sku, p.isActive,
                   p.brandId, p.noOfViews, p.noOfSold,
                   p.categoryId, p.imageId,
                   ops.id as optionId,
                   ops.price,
                   inv.quantity as stock,
                   img.url as imageUrl
            FROM products p
            JOIN categories cate ON cate.id = p.categoryId
            JOIN option_variant ops ON ops.productId = p.id
            JOIN inventory inv ON inv.optionVariantId = ops.id
            JOIN image img ON p.imageId = img.id
            WHERE cate.id = :categoryId
            AND ops.price = (
                SELECT MIN(o.price)
                FROM option_variant o
                JOIN inventory i ON i.optionVariantId = o.id
                WHERE o.productId = p.id AND i.quantity > 0
            )
            """)
    List<Product> getProductsByCategory(@Bind("categoryId") int categoryId);

    @SqlQuery("""
        SELECT p.id, p.name, p.description, p.sku, p.isActive,
               p.brandId, p.noOfViews, p.noOfSold,
               p.categoryId, p.imageId,
               ops.id as optionId,
               ops.price,
               COALESCE(inv.quantity, 0) as stock,
               img.url as imageUrl
        FROM products p
        JOIN option_variant ops ON ops.productId = p.id
        LEFT JOIN inventory inv ON inv.optionVariantId = ops.id
        JOIN image img ON p.imageId = img.id
        WHERE p.id = :id
          AND p.isActive = true
        ORDER BY
          CASE WHEN COALESCE(inv.quantity, 0) > 0 THEN 0 ELSE 1 END,
          ops.price ASC
        LIMIT 1
        """)
    Product getProductById(@Bind("id") int id);

    @SqlQuery("SELECT p.id, p.name, p.description, p.sku, p.isActive, " +
            "       p.categoryId, p.brandId, p.noOfViews, p.noOfSold, " +
            "       p.imageId as imageId, " +
            "       ops.id as optionId, ops.price as price, " +
            "       COALESCE(inv.quantity, 0) as stock, " +
            "       img.url as imageUrl, " +
            "       GROUP_CONCAT(CONCAT(v.name, ': ', v.value) SEPARATOR ' | ') as variantText " +
            "FROM products as p " +
            "INNER JOIN categories as cate on cate.id = p.categoryId " +
            "INNER JOIN option_variant as ops on ops.productId = p.id " +
            "LEFT JOIN inventory inv ON inv.optionVariantId = ops.id " +
            "INNER JOIN image as img on p.imageId = img.id " +
            "LEFT JOIN variant v ON v.optionId = ops.id " +
            "WHERE p.id = :productId " +
            "AND ops.id = :optionId " +
            "AND p.isActive = true " +
            "GROUP BY p.id, p.name, p.description, p.sku, p.isActive, " +
            "p.categoryId, p.brandId, p.noOfViews, p.noOfSold, p.imageId, " +
            "ops.id, ops.price, inv.quantity, img.url")
    Product getProductByIdAndOptionId(@Bind("productId") int productId,
                                      @Bind("optionId") int optionId);

    @SqlQuery("""
            SELECT p.id, p.name, p.sku, p.description, p.isActive,
                   p.categoryId, cate.name as categoryName,
                   p.brandId, p.noOfViews, p.noOfSold,
                   p.imageId, img.url as imageUrl,
                   ops.price, inv.quantity as stock, ops.id as optionId
            FROM products p
            JOIN categories cate ON cate.id = p.categoryId
            JOIN option_variant ops ON ops.productId = p.id
            JOIN inventory inv ON inv.optionVariantId = ops.id
            JOIN image img ON img.id = p.imageId
            WHERE p.isActive = true AND inv.quantity > 0
            """)
    List<Product> getAllProducts();

    @SqlQuery("""
        SELECT o.price
        FROM option_variant o
        LEFT JOIN inventory i ON i.optionVariantId = o.id
        WHERE o.productId = :productId
        ORDER BY 
          CASE WHEN COALESCE(i.quantity, 0) > 0 THEN 0 ELSE 1 END,
          o.price ASC
        LIMIT 1
        """)
    Integer getMinimumPriceForProduct(@Bind("productId") int productId);

    @SqlQuery("""
        SELECT o.price
        FROM option_variant o
        WHERE o.id = :optionId
        """)
    Integer getPriceForOption(@Bind("optionId") int optionId);

    @SqlUpdate("INSERT INTO products (name,description, isActive, categoryId, brandId, noOfViews, noOfSold, imageId, sku) "
            + "VALUES (:name, :description,COALESCE(:isActive, 1), :categoryId, :brandId, 0, 0, COALESCE(:imageId, NULL), :sku)")
    @GetGeneratedKeys
    int addProduct(@Bind("name") String name,
                   @Bind("description") String description,
                   @Bind("isActive") Boolean isActive,
                   @Bind("categoryId") Integer categoryId,
                   @Bind("brandId") Integer brandId,
                   @Bind("imageId") Integer imageId,
                   @Bind("sku") String sku);

    @SqlQuery("""
            SELECT p.id, p.name, p.description, p.sku, p.isActive,
                   p.brandId, p.noOfViews, p.noOfSold,
                   p.categoryId, p.imageId,
                   ops.id as optionId,
                   ops.price,
                   inv.quantity as stock,
                   img.url as imageUrl
            FROM products p
            JOIN option_variant ops ON ops.productId = p.id
            JOIN inventory inv ON inv.optionVariantId = ops.id
            JOIN image img ON p.imageId = img.id
            WHERE p.isActive = true
            AND LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')
            AND ops.price = (
                SELECT MIN(o.price)
                FROM option_variant o
                JOIN inventory i ON i.optionVariantId = o.id
                WHERE o.productId = p.id AND i.quantity > 0
            )
            """)
    List<Product> searchProducts(@Bind("name") String name);

    @SqlQuery(value = "SELECT p.id           as id, " +
            "       p.name         as name, " +
            "       p.description  as description, " +
            "       p.sku          as sku, " +
            "       p.isActive     as isActive, " +
            "       p.brandId      as brandId, " +
            "       p.noOfViews    as noOfViews, " +
            "       p.noOfSold     as noOfSold, " +
            "       p.categoryId   as categoryId, " +
            "       p.imageId      as imageId, " +
            "       ops.id         as optionId, " +
            "       ops.price      as price, " +
            "       inv.quantity   as stock, " +
            "       img.url        as imageUrl " +
            "FROM products as p " +
            "INNER JOIN categories as cate on cate.id = p.categoryId " +
            "INNER JOIN option_variant as ops on ops.productId = p.id " +
            "INNER JOIN inventory inv ON inv.optionVariantId = ops.id " +
            "INNER JOIN image as img on p.imageId = img.id " +
            "WHERE cate.id = :categoryId " +
            "AND ops.price = (SELECT MIN(o.price) " +
            "                 FROM option_variant o " +
            "                 INNER JOIN inventory i ON i.optionVariantId = o.id " +
            "                 WHERE p.id = o.productId " +
            "                 AND i.quantity > 0 " +
            "                 AND p.isActive = true ) " +
            "ORDER BY p.noOfViews DESC, p.noOfSold DESC " +
            "LIMIT 3")
    @RegisterConstructorMapper(Product.class)
    public List<Product> getTopProductsByCategoryId(@Bind("categoryId") int categoryId,
                                                    @Bind("limit") Integer limit);

    @SqlUpdate("UPDATE products SET isActive = false WHERE id = :id")
    boolean deactivateProduct(@Bind("id") int id);

    @SqlQuery(value = """
                SELECT p.id as id, p.name as name, p.description as description,
                       p.sku as sku, p.isActive as isActive, p.brandId as brandId,
                       p.noOfViews as noOfViews, p.noOfSold as noOfSold,
                       p.categoryId as categoryId, p.imageId as imageId,
                       ops.id as optionId, ops.price as price,
                       inv.quantity as stock,
                       img.url as imageUrl,
                       v.id as variantId,
                       v.value as variantValue,
                       v.name as variantName
                FROM products p
                    INNER JOIN categories cate ON cate.id = p.categoryId
                    INNER JOIN option_variant ops ON ops.productId = p.id
                    INNER JOIN inventory inv ON inv.optionVariantId = ops.id
                    INNER JOIN image img ON p.imageId = img.id
                    INNER JOIN variant v ON ops.id = v.optionId
                WHERE p.id = :id
                  AND ops.price = (
                        SELECT MIN(o.price)
                        FROM option_variant o
                        JOIN inventory i ON i.optionVariantId = o.id
                        WHERE p.id = o.productId AND i.quantity > 0
                  )
            """)
    @RegisterConstructorMapper(Product.class)
    Product editProduct(@Bind("id") int id);

    @SqlQuery("""
                SELECT p.id as id, p.name as name, p.description as description,
                       p.sku as sku, p.isActive as isActive, p.brandId as brandId,
                       p.noOfViews as noOfViews, p.noOfSold as noOfSold,
                       p.categoryId as categoryId, p.imageId as imageId,
                       ops.id as optionId, ops.price as price,
                       inv.quantity as stock,
                       img.url as imageUrl,
                       v.id as variantId,
                       v.value as variantValue,
                       v.name as variantName
                FROM products p
                    INNER JOIN categories cate ON cate.id = p.categoryId
                    INNER JOIN option_variant ops ON ops.productId = p.id
                    INNER JOIN inventory inv ON inv.optionVariantId = ops.id
                    INNER JOIN image img ON p.imageId = img.id
                    INNER JOIN variant v ON ops.id = v.optionId
                WHERE p.id = :id
                  AND ops.price = (
                        SELECT MIN(o.price)
                        FROM option_variant o
                        JOIN inventory i ON i.optionVariantId = o.id
                        WHERE p.id = o.productId AND i.quantity > 0
                  )
            """)
    @RegisterConstructorMapper(Variant.class)
    List<Variant> getVariants(@Bind("id") int id);

    @SqlUpdate(value = "update products\n" +
            "set noOfViews = noOfViews +1\n" +
            "where id = :id;")
    Boolean increaseNoOfViews(@Bind("id") int id);

    @SqlUpdate(value = "update products\n" +
            "set noOfSold = noOfSold + :quantity\n" +
            "where id = :id ;\n")
    Boolean increaseNoOfSold(@Bind("id") int id, @Bind("quantity") Integer quantity);

    @SqlQuery(value = """
            SELECT p.id, p.name, p.noOfViews, p.noOfSold, p.sku, p.isActive,
                   p.brandId, p.categoryId, p.imageId,
                   img.url as imageUrl,
                   ops.id as optionId,
                   ops.price,
                   inv.quantity as stock
            FROM products p
            INNER JOIN option_variant ops ON ops.productId = p.id
            INNER JOIN inventory inv ON inv.optionVariantId = ops.id
            INNER JOIN image img ON p.imageId = img.id
            WHERE p.isActive = true
            AND ops.price = (
                SELECT MIN(o.price)
                FROM option_variant o
                JOIN inventory i ON i.optionVariantId = o.id
                WHERE p.id = o.productId AND i.quantity > 0
            )
            ORDER BY p.noOfSold DESC, p.noOfViews DESC
            LIMIT 10
            """)
    List<Product> getTopProducts();

    @SqlQuery("""
            SELECT p.id, p.name, p.description, p.sku, p.isActive,
                   p.brandId, p.noOfViews, p.noOfSold,
                   p.categoryId, p.imageId,
                   ops.id as optionId,
                   ops.price,
                   inv.quantity as stock,
                   img.url as imageUrl
            FROM products p
            JOIN option_variant ops ON ops.productId = p.id
            JOIN inventory inv ON inv.optionVariantId = ops.id
            JOIN image img ON p.imageId = img.id
            WHERE p.categoryId = :categoryId
              AND p.isActive = true
              AND (:minPrice IS NULL OR ops.price >= :minPrice)
              AND (:maxPrice IS NULL OR ops.price <= :maxPrice)
              AND (:brandId IS NULL OR p.brandId = :brandId)
            
              AND ops.id = (
                  SELECT o.id
                  FROM option_variant o
                  JOIN inventory i ON i.optionVariantId = o.id
                  WHERE o.productId = p.id
                    AND i.quantity > 0
                  ORDER BY o.price ASC
                  LIMIT 1
              )
            
            ORDER BY
              CASE WHEN :sort = 'price_asc' THEN ops.price END ASC,
              CASE WHEN :sort = 'price_desc' THEN ops.price END DESC,
              p.noOfSold DESC
            
            LIMIT :size OFFSET :offset
            """)
    List<Product> filterProducts(
            @Bind("categoryId") int categoryId,
            @Bind("minPrice") Integer minPrice,
            @Bind("maxPrice") Integer maxPrice,
            @Bind("brandId") Integer brandId,
            @Bind("sort") String sort,
            @Bind("size") Integer size,
            @Bind("offset") Integer offset
    );

    @SqlUpdate("""
            UPDATE products 
            SET name = :name,
                description = :description,
                sku = :sku,
                categoryId = :categoryId,
                brandId = :brandId,
                primaryImage = COALESCE(:primaryImage, primaryImage),
                height = :height,
                length = :length,
                width = :width,
                weight = :weight
            WHERE id = :id
            """)
    boolean updateProduct(@Bind("id") Integer id,
                          @Bind("name") String name,
                          @Bind("description") String description,
                          @Bind("sku") String sku,
                          @Bind("categoryId") Integer categoryId,
                          @Bind("brandId") Integer brandId,
                          @Bind("primaryImage") Integer primaryImage,
                          @Bind("height") Integer height,
                          @Bind("length") Integer length,
                          @Bind("width") Integer width,
                          @Bind("weight") Integer weight);

    @SqlQuery("""
    SELECT COUNT(DISTINCT p.id)
    FROM products p
    JOIN option_variant ops ON ops.productId = p.id
    JOIN inventory inv ON inv.optionVariantId = ops.id
    WHERE p.categoryId = :categoryId
      AND p.isActive = true
      AND (:minPrice IS NULL OR ops.price >= :minPrice)
      AND (:maxPrice IS NULL OR ops.price <= :maxPrice)
      AND (:brandId IS NULL OR p.brandId = :brandId)
      AND inv.quantity > 0
    """)
    int countProducts(@Bind("categoryId") int categoryId,
                      @Bind("minPrice") Integer minPrice,
                      @Bind("maxPrice") Integer maxPrice,
                      @Bind("brandId") Integer brandId);
}