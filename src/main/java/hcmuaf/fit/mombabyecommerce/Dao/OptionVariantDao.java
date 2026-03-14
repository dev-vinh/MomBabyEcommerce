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

    @SqlUpdate("INSERT INTO option_variant (productId, price, stock) VALUES (:productId, :price, :stock)")
    @GetGeneratedKeys
    int createOption(@Bind("productId") Integer productId, @Bind("price") Integer price,
                     @Bind("stock") Integer stock);

    @SqlQuery(value = "select *\n" +
            "from option_variant\n" +
            "where id = :id;")
    OptionVariant getOptionById(@Bind("id") Integer id);

    @SqlUpdate(value = "update option_variant\n" +
            "set\n" +
            "    stock = :stock " +
            "where id = :id")
    Boolean updateStock(@Bind("id") Integer id, @Bind("stock") Integer stock);

    @SqlQuery(value = "select *\n" +
            "from option_variant\n" +
            "where productId = :productId")
    List<OptionVariant> getOptionsByProductId(@Bind("productId") Integer productId);

    @SqlQuery(value = "select\n" +
            "    o.id as id, o.productId, o.price, o.stock,\n" +
            "    v.id as variantId, v.name as variantName,\n" +
            "    v.value as variantValue \n" +
            "from\n" +
            "    option_variant as o\n" +
            "    inner join variant as  v\n" +
            "        on o.id = v.optionId\n" +
            "where o.id in (<optionIds>)\n")
    List<OptionVariant> getVariantByOptionId(@BindList("optionIds") List<Integer> optionIds);

    @SqlUpdate("""
            UPDATE option_variant 
            SET price = :price,
                stock = :stock
            WHERE id = :id
            """)
    boolean updateOption(@Bind("id") Integer id,
                         @Bind("price") Integer price,
                         @Bind("stock") Integer stock);
}
