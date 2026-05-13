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


    @SqlUpdate("INSERT INTO option_variant (productId, price) VALUES (:productId, :price)")
    @GetGeneratedKeys
    int createOption(@Bind("productId") Integer productId, @Bind("price") Integer price);


    @SqlUpdate("INSERT INTO inventory (optionVariantId, quantity) VALUES (:optionVariantId, :quantity)")
    void createInventory(@Bind("optionVariantId") Integer optionVariantId, @Bind("quantity") Integer quantity);

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
        WHERE o.id = :id
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

    @SqlUpdate("update inventory\n" +
            "set\n" +
            "    quantity = :quantity " +
            "where optionVariantId = :optionVariantId")
    Boolean updateStock(@Bind("optionVariantId") Integer optionVariantId, @Bind("quantity") Integer quantity);

    @SqlQuery(value = "select *\n" +
            "from option_variant\n" +
            "where productId = :productId")
    List<OptionVariant> getOptionsByProductId(@Bind("productId") Integer productId);

    @SqlQuery(value = "select\n" +
            "    o.id as id, o.productId, o.price,\n" +
            "    SUM(inv.quantity) as stock,\n" +
            "    v.id as variantId, v.name as variantName,\n" +
            "    v.value as variantValue \n" +
            "from\n" +
            "    option_variant as o\n" +
            "    inner join variant as v\n" +
            "        on o.id = v.optionId\n" +
            "    inner join inventory as inv\n" +
            "        on inv.optionVariantId = o.id\n" +
            "where o.id in (<optionIds>)\n" +
            "GROUP BY o.id, o.productId, o.price, v.id, v.name, v.value\n")
    List<OptionVariant> getVariantByOptionId(@BindList("optionIds") List<Integer> optionIds);

    @SqlUpdate("""
            UPDATE option_variant 
            SET price = :price
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
    ORDER BY o.id, v.id
""")
    List<OptionVariant> getOptionDetailsByProductId(@Bind("productId") Integer productId);
}