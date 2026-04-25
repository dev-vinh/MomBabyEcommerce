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

    @SqlQuery(value = " " +
            "SELECT p.id as id, p.name as name, p.description as description, " +
            "            p.sku as sku, p.isActive as isActive, p.brandId as brandId,  " +
            "            p.noOfViews as noOfViews, p.noOfSold as noOfSold,  " +
            "            p.categoryId as categoryId, p.imageId as imageId, " +
            "            ops.id as optionId ,ops.price as price, " +
            "            SUM(inv.quantity) as stock, " +                                     
            "            img.url as imageUrl  " +
            "            FROM products as p " +
            "                INNER JOIN categories as cate on cate.id = p.categoryId " +
            "                INNER JOIN `option_variant` as ops on ops.productId = p.id " +
            "                inner join image as img on p.imageId = img.id " +
            "                INNER JOIN inventory as inv ON inv.optionVariantId = ops.id " + 
            "            WHERE cate.id= :categoryId and ops.price = ( " +
            "                    SELECT MIN(price) " +
            "                    FROM option_variant as ops " +
            "                    WHERE p.id = ops.productId " +
            "                    AND EXISTS (SELECT 1 FROM inventory inv2 WHERE inv2.optionVariantId = ops.id AND inv2.quantity > 0) " + // ✅ SỬA
            "                       and p.isActive = true ) " +
            "GROUP BY p.id, ops.id, img.url")                                                
    @RegisterConstructorMapper(Product.class)
    List<Product> getProductsByCategory(@Bind("categoryId") int categoryId);

    @SqlQuery(value = "SELECT p.id           as id,\n" +
            "       p.name         as name,\n" +
            "       p.description  as description,\n" +
            "       p.sku          as sku,\n" +
            "       p.isActive    as isActive,\n" +
            "       p.brandId     as brandId,\n" +
            "       p.noOfViews  as noOfViews,\n" +
            "       p.noOfSold   as noOfSold,\n" +
            "       p.categoryId  as categoryId,\n" +
            "       p.imageId     as imageId,\n" +
            "       ops.id         as optionId,\n" +
            "       ops.price      as price,\n" +
            "       SUM(inv.quantity) as stock,\n" +                                         
            "       img.url        as imageUrl \n" +
            "FROM products as p\n" +
            "         INNER JOIN categories as cate on cate.id = p.categoryId\n" +
            "         INNER JOIN `option_variant` as ops on ops.productId = p.id\n" +
            "         inner join image as img on p.imageId = img.id\n" +
            "         INNER JOIN inventory as inv ON inv.optionVariantId = ops.id\n" +       
            "WHERE p.id = :id" +
            "  and ops.price = (SELECT MIN(price)\n" +
            "                   FROM option_variant as ops\n" +
            "                   WHERE p.id = ops.productId\n" +
            "                   AND EXISTS (SELECT 1 FROM inventory inv2 WHERE inv2.optionVariantId = ops.id AND inv2.quantity > 0))\n" + // ✅ SỬA
            "GROUP BY p.id, ops.id, img.url")                                                
    @RegisterConstructorMapper(Product.class)
    Product getProductById(@Bind("id") int id);

    @SqlQuery(value = "SELECT p.id as id, p.name as name, p.description as description, " +
            "         p.isActive as isActive, " +
            "       p.noOfViews as noOfViews, p.noOfSold as noOfSold, " +
            "        p.imageId as imageId, " +
            "       ops.id as optionId ,ops.price as price, " +
            "       SUM(inv.quantity) as stock, " +                                          
            "       img.url as imageUrl " +
            "FROM products as p " +
            "         INNER JOIN categories as cate on cate.id = p.categoryId " +
            "         INNER JOIN `option_variant` as ops on ops.productId = p.id " +
            "         inner join image as img on p.imageId = img.id " +
            "         INNER JOIN inventory as inv ON inv.optionVariantId = ops.id " +       
            "WHERE p.id= :productId and ops.id =:optionId " +
            "GROUP BY p.id, ops.id, img.url;")                                              

    @RegisterConstructorMapper(Product.class)
    Product getProductByIdAndOptionId(@Bind("productId") int productId,
                                      @Bind("optionId") int optionId);

    @SqlQuery(value = "SELECT p.id, p.name, p.sku, p.description, p.isActive, " +
            "       p.categoryId, cate.name as categoryName, " +
            "       p.brandId, p.noOfViews, p.noOfSold, " +
            "       p.imageId, img.url as imageUrl, " +
            "       ops.price, ops.id as optionId, " +
            "       SUM(inv.quantity) as stock " +                                           
            "FROM products p " +
            "         INNER JOIN categories cate ON cate.id = p.categoryId " +
            "         INNER JOIN option_variant ops ON ops.productId = p.id " +
            "         INNER JOIN image img ON img.id = p.imageId " +
            "         INNER JOIN inventory inv ON inv.optionVariantId = ops.id " +          
            "WHERE p.isActive = true " +
            "GROUP BY p.id, ops.id, img.url " +                                             
            "HAVING SUM(inv.quantity) > 0")                                                  
    @RegisterConstructorMapper(Product.class)
    List<Product> getAllProducts();

    @SqlQuery("SELECT ops.price FROM option_variant ops WHERE ops.productId = :productId " +
            "AND EXISTS (SELECT 1 FROM inventory inv WHERE inv.optionVariantId = ops.id AND inv.quantity > 0) " +
            "ORDER BY ops.price ASC LIMIT 1")
    Integer getMinimumPriceForProduct(@Bind("productId") int productId);

    @SqlQuery("SELECT ops.price FROM option_variant ops WHERE ops.id = :optionId " +        
            "AND EXISTS (SELECT 1 FROM inventory inv WHERE inv.optionVariantId = ops.id AND inv.quantity > 0)")
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
                        SELECT p.id           as id,
                               p.name         as name,
                               p.description  as description,
                               p.sku          as sku,
                               p.isActive     as isActive,
                               p.brandId      as brandId,
                               p.noOfViews    as noOfViews,
                               p.noOfSold     as noOfSold,
                               p.categoryId   as categoryId,
                               p.imageId      as imageId,
                               ops.id         as optionId,
                               ops.price      as price,
                               SUM(inv.quantity) as stock,
                               img.url        as imageUrl
                        FROM products as p
                                 INNER JOIN `option_variant` as ops on ops.productId = p.id
                                 INNER JOIN image as img on p.imageId = img.id
                                 INNER JOIN inventory as inv ON inv.optionVariantId = ops.id
                        WHERE p.isActive = true
                          AND LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')
                          AND ops.price = (SELECT MIN(price)
                                           FROM option_variant as ops
                                           WHERE p.id = ops.productId
                                             AND EXISTS (SELECT 1 FROM inventory inv2 WHERE inv2.optionVariantId = ops.id AND inv2.quantity > 0))
                        GROUP BY p.id, ops.id, img.url
                     """)
    @RegisterConstructorMapper(Product.class)
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
            "       p.imageId as imageId, " +
            "       ops.id         as optionId, " +
            "       ops.price      as price, " +
            "       SUM(inv.quantity) as stock, " +                                          
            "       img.url        as imageUrl " +
            "FROM products as p " +
            "         INNER JOIN categories as cate on cate.id = p.categoryId " +
            "         INNER JOIN `option_variant` as ops on ops.productId = p.id " +
            "         inner join image as img on p.imageId = img.id " +
            "         INNER JOIN inventory as inv ON inv.optionVariantId = ops.id " +         
            "WHERE cate.id = :categoryId " +
            "  and p.isActive = true " +
            "  and ops.price = (SELECT MIN(price) " +
            "                   FROM option_variant as ops " +
            "                   WHERE p.id = ops.productId " +
            "                   AND EXISTS (SELECT 1 FROM inventory inv2 WHERE inv2.optionVariantId = ops.id AND inv2.quantity > 0) " + // ✅ SỬA
            "                     and p.isActive = true ) " +
            "GROUP BY p.id, ops.id, img.url " +                                               
            "order by p.noOfViews desc , p.noOfSold desc " +
            "limit 3")
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
                                SUM(inv.quantity) as stock,
                                img.url as imageUrl,
                                v.id as variantId,
                                v.value as variantValue,
                                v.name as variantName
                         FROM products as p
                             INNER JOIN categories as cate on cate.id = p.categoryId
                             INNER JOIN `option_variant` as ops on ops.productId = p.id
                             INNER JOIN image as img on p.imageId = img.id
                             INNER JOIN inventory as inv ON inv.optionVariantId = ops.id
                             INNER JOIN variant as v on ops.id = v.optionId
                         WHERE p.id = :id
                           AND ops.price = (
                                 SELECT MIN(price)
                                 FROM option_variant as ops
                                 WHERE p.id = ops.productId
                                   AND EXISTS (SELECT 1 FROM inventory inv2 WHERE inv2.optionVariantId = ops.id AND inv2.quantity > 0)
                           )
                         GROUP BY p.id, ops.id, img.url, v.id
                     """)
    @RegisterConstructorMapper(Product.class)
    Product editProduct(@Bind("id") int id);

    @SqlQuery("""
                         SELECT p.id as id, p.name as name, p.description as description,
                                p.sku as sku, p.isActive as isActive, p.brandId as brandId,
                                p.noOfViews as noOfViews, p.noOfSold as noOfSold,
                                p.categoryId as categoryId, p.imageId as imageId,
                                ops.id as optionId, ops.price as price,
                                SUM(inv.quantity) as stock,
                                img.url as imageUrl,
                                v.id as variantId,
                                v.value as variantValue,
                                v.name as variantName
                         FROM products as p
                             INNER JOIN categories as cate on cate.id = p.categoryId
                             INNER JOIN `option_variant` as ops on ops.productId = p.id
                             INNER JOIN image as img on p.imageId = img.id
                             INNER JOIN inventory as inv ON inv.optionVariantId = ops.id
                             INNER JOIN variant as v on ops.id = v.optionId
                         WHERE p.id = :id
                           AND ops.price = (
                                 SELECT MIN(price)
                                 FROM option_variant as ops
                                 WHERE p.id = ops.productId
                                   AND EXISTS (SELECT 1 FROM inventory inv2 WHERE inv2.optionVariantId = ops.id AND inv2.quantity > 0)
                         )
                         GROUP BY p.id, ops.id, img.url, v.id
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

    @SqlQuery(value = "SELECT p.id           as id,\n" +
            "       p.name         as name,\n" +
            "       p.noOfViews    as noOfViews,\n" +
            "       p.noOfSold     as noOfSold,\n" +
            "       p.sku          as sku,\n" +
            "       p.isActive     as isActive,\n" +
            "       p.brandId      as brandId,\n" +
            "       p.categoryId   as categoryId,\n" +
            "       p.imageId as imageId,\n" +
            "       img.url        as imageUrl,\n" +
            "       ops.id         as optionId,\n" +
            "       ops.price      as price,\n" +
            "       SUM(inv.quantity) as stock\n" +    
            "FROM products as p\n" +
            "         INNER JOIN `option_variant` as ops on ops.productId = p.id\n" +
            "         inner join image as img on p.imageId = img.id\n" +
            "         INNER JOIN inventory as inv ON inv.optionVariantId = ops.id\n" +          
            "where p.isActive = true\n" +
            "  and ops.price = (SELECT MIN(price)\n" +
            "                   FROM option_variant as ops\n" +
            "                   WHERE p.id = ops.productId\n" +
            "                   AND EXISTS (SELECT 1 FROM inventory inv2 WHERE inv2.optionVariantId = ops.id AND inv2.quantity > 0))\n" + // ✅ SỬA
            "GROUP BY p.id, ops.id, img.url\n" +                                         
            "order by p.noOfSold desc, p.noOfViews desc\n" +
            "limit 10\n")
    List<Product> getTopProducts();

    @SqlQuery("""
                         SELECT p.id           as id,
                                p.name         as name,
                                p.description  as description,
                                p.sku          as sku,
                                p.isActive     as isActive,
                                p.brandId      as brandId,
                                p.noOfViews    as noOfViews,
                                p.noOfSold     as noOfSold,
                                p.categoryId   as categoryId,
                                p.imageId      as imageId,
                                ops.id         as optionId,
                                ops.price      as price,
                                SUM(inv.quantity) as stock,
                                img.url        as imageUrl
                         FROM products as p
                                  INNER JOIN `option_variant` as ops on ops.productId = p.id
                                  INNER JOIN image as img on p.imageId = img.id
                                  INNER JOIN inventory as inv ON inv.optionVariantId = ops.id
                         WHERE p.categoryId = :categoryId
                           AND p.isActive = true
                           AND (:minPrice IS NULL OR ops.price >= :minPrice)
                           AND (:maxPrice IS NULL OR ops.price <= :maxPrice)
                           AND ops.id = (
                               SELECT id FROM option_variant
                               WHERE productId = p.id
                                 AND (:minPrice IS NULL OR price >= :minPrice)
                                 AND (:maxPrice IS NULL OR price <= :maxPrice)
                                 AND EXISTS (SELECT 1 FROM inventory inv3 WHERE inv3.optionVariantId = option_variant.id AND inv3.quantity > 0)
                               ORDER BY price ASC LIMIT 1
                           )
                         GROUP BY p.id, ops.id, img.url
                     """)
    @RegisterConstructorMapper(Product.class)
    List<Product> filterProducts(@Bind("categoryId") int categoryId,
                                 @Bind("minPrice") @Nullable Integer minPrice,
                                 @Bind("maxPrice") @Nullable Integer maxPrice);

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
}