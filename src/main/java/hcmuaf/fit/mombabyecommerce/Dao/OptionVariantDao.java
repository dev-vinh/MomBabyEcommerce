package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.OptionVariant;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterConstructorMapper(OptionVariant.class)
public interface OptionVariantDao {

    @SqlUpdate("""
        INSERT INTO option_variant (productId, price, isActive)
        VALUES (:productId, :price, 1)
        """)
    @GetGeneratedKeys
    int createOption(@Bind("productId") Integer productId,
                     @Bind("price") Integer price);

    @SqlUpdate("""
        INSERT INTO inventory (optionVariantId, quantity)
        VALUES (:optionVariantId, :quantity)
        ON DUPLICATE KEY UPDATE quantity = quantity
        """)
    void createInventory(@Bind("optionVariantId") Integer optionVariantId,
                         @Bind("quantity") Integer quantity);

    @SqlQuery("""
        SELECT
            o.id as id,
            o.productId,
            o.price,
            COALESCE(inv.quantity, 0) as stock,
            inv.warehouseLocation as warehouseLocation,
            CAST(NULL AS SIGNED) as variantId,
            CAST(NULL AS CHAR) as variantName,
            CAST(NULL AS CHAR) as variantValue
        FROM option_variant o
        LEFT JOIN inventory inv ON inv.optionVariantId = o.id
        WHERE o.id = :id
          AND o.isActive = 1
        """)
    OptionVariant getOptionById(@Bind("id") Integer id);

    @SqlQuery("""
        SELECT COALESCE(quantity, 0)
        FROM inventory
        WHERE optionVariantId = :optionVariantId
        """)
    Integer getStockByOptionId(@Bind("optionVariantId") Integer optionVariantId);

    @SqlUpdate("""
        UPDATE inventory
        SET quantity = quantity - :quantity
        WHERE optionVariantId = :optionVariantId
          AND quantity >= :quantity
        """)
    boolean decreaseStockIfEnough(@Bind("optionVariantId") Integer optionVariantId,
                                  @Bind("quantity") Integer quantity);

    @SqlUpdate("""
        UPDATE inventory
        SET quantity = :quantity
        WHERE optionVariantId = :optionVariantId
        """)
    Boolean updateStock(@Bind("optionVariantId") Integer optionVariantId,
                        @Bind("quantity") Integer quantity);

    @SqlQuery("""
        SELECT
            o.id as id,
            o.productId,
            o.price,
            COALESCE(inv.quantity, 0) as stock,
            CAST(NULL AS SIGNED) as variantId,
            CAST(NULL AS CHAR) as variantName,
            CAST(NULL AS CHAR) as variantValue
        FROM option_variant o
        LEFT JOIN inventory inv ON inv.optionVariantId = o.id
        WHERE o.productId = :productId
          AND o.isActive = 1
        ORDER BY o.id
        """)
    List<OptionVariant> getOptionsByProductId(@Bind("productId") Integer productId);

    @SqlQuery("""
        SELECT
            o.id as id,
            o.productId,
            o.price,
            COALESCE(inv.quantity, 0) as stock,
            v.id as variantId,
            v.name as variantName,
            v.value as variantValue
        FROM option_variant as o
        LEFT JOIN inventory as inv ON inv.optionVariantId = o.id
        LEFT JOIN variant as v ON o.id = v.optionId
        WHERE o.id in (<optionIds>)
          AND o.isActive = 1
        ORDER BY o.id, v.id
        """)
    List<OptionVariant> getVariantByOptionId(@BindList("optionIds") List<Integer> optionIds);

    @SqlUpdate("""
        UPDATE option_variant
        SET price = :price,
            isActive = 1
        WHERE id = :id
        """)
    boolean updateOption(@Bind("id") Integer id,
                         @Bind("price") Integer price);

    @SqlUpdate("""
        UPDATE inventory
        SET quantity = :quantity
        WHERE optionVariantId = :optionVariantId
        """)
    boolean updateOptionStock(@Bind("optionVariantId") Integer optionVariantId,
                              @Bind("quantity") Integer quantity);

    @SqlUpdate("""
        UPDATE option_variant
        SET isActive = 0
        WHERE productId = :productId
          AND id NOT IN (<keepOptionIds>)
        """)
    int deactivateOptionsNotIn(@Bind("productId") Integer productId,
                               @BindList("keepOptionIds") List<Integer> keepOptionIds);

    @SqlUpdate("""
        UPDATE option_variant
        SET isActive = 0
        WHERE productId = :productId
        """)
    int deactivateAllOptionsByProductId(@Bind("productId") Integer productId);

    @SqlQuery("""
        SELECT COUNT(*)
        FROM option_variant
        WHERE id = :optionId
          AND productId = :productId
        """)
    int countOptionBelongsToProduct(@Bind("productId") Integer productId,
                                    @Bind("optionId") Integer optionId);

    @SqlQuery("""
        SELECT
            o.id as id,
            o.productId,
            o.price,
            COALESCE(inv.quantity, 0) as stock,
            v.id as variantId,
            v.name as variantName,
            v.value as variantValue
        FROM option_variant o
        LEFT JOIN inventory inv ON inv.optionVariantId = o.id
        LEFT JOIN variant v ON v.optionId = o.id
        WHERE o.productId = :productId
          AND o.isActive = 1
        ORDER BY o.id, v.id
        """)
    List<OptionVariant> getOptionDetailsByProductId(@Bind("productId") Integer productId);
    @SqlQuery("""
    SELECT
        o.id as id,
        o.productId as productId,
        o.price as price,
        COALESCE(inv.quantity, 0) as stock,
        inv.warehouseLocation as warehouseLocation,
        v.id as variantId,
        v.name as variantName,
        v.value as variantValue
    FROM option_variant o
    LEFT JOIN inventory inv ON inv.optionVariantId = o.id
    LEFT JOIN variant v ON v.optionId = o.id
    WHERE o.isActive = 1
    ORDER BY o.productId, o.id, v.id
    """)
    List<OptionVariant> getAllOptionsWithStock();
    @SqlUpdate("""
    UPDATE inventory
    SET quantity = :quantity,
        warehouseLocation = :location,
        lastStockIn = NOW()
    WHERE optionVariantId = :optionVariantId
    """)
    boolean updateStockWithLocation(@Bind("optionVariantId") Integer optionVariantId,
                                    @Bind("quantity") Integer quantity,
                                    @Bind("location") String location);
    @SqlQuery("""
    SELECT
        o.id as id,
        o.productId,
        o.price,
        COALESCE(inv.quantity, 0) as stock,
          inv.warehouseLocation as warehouseLocation,
        v.id as variantId,
        v.name as variantName,
        v.value as variantValue
    FROM option_variant o
    LEFT JOIN inventory inv ON inv.optionVariantId = o.id
    LEFT JOIN variant v ON v.optionId = o.id
    WHERE o.isActive = 1
      AND o.productId = :productId
    ORDER BY o.id, v.id
    """)
    List<OptionVariant> getOptionsWithStockByProductId(@Bind("productId") Integer productId);
}
