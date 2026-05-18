package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Variant;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterConstructorMapper(Variant.class)
public interface VariantDao {

    @SqlQuery("SELECT * FROM variant")
    List<Variant> getAllVariants();

    @SqlQuery("SELECT * FROM variant WHERE id = :id")
    Variant getVariantById(@Bind("id") Integer id);

    @SqlQuery("""
        SELECT MIN(v.id) as id,
               v.categoryId as categoryId,
               v.name as name,
               v.value as value,
               CAST(NULL AS SIGNED) as optionId
        FROM attributes a
        JOIN variant v ON v.categoryId = a.categoryId
                      AND v.name = a.name
        WHERE a.id = :id
          AND v.value IS NOT NULL
        GROUP BY v.categoryId, v.name, v.value
        ORDER BY v.value
        """)
    List<Variant> getVariantValuesByVariantId(@Bind("id") Integer id);

    @SqlQuery("""
        SELECT MIN(id) as id,
               categoryId,
               name,
               CAST(NULL AS CHAR) as value,
               CAST(NULL AS SIGNED) as optionId
        FROM variant
        WHERE optionId IS NULL
          AND (categoryId IS NULL OR categoryId = :categoryId)
          AND name IS NOT NULL
        GROUP BY categoryId, name
        ORDER BY name
        """)
    List<Variant> getVariantsByCategoryId(@Bind("categoryId") Integer categoryId);


    @SqlUpdate("INSERT INTO variant (name, categoryId) VALUES (:name, :categoryId)")
    @GetGeneratedKeys("id")
    int createVariant(@Bind("name") String name, @Bind("categoryId") Integer categoryId);

    @SqlUpdate("""
        INSERT INTO variant (name, value, categoryId, optionId)
        SELECT name, value, categoryId, :optionId
        FROM variant
        WHERE id = :variantId
        """)
    int addOptionVariantValue(@Bind("optionId") Integer optionId,
                              @Bind("variantId") Integer variantId);
    @SqlUpdate("DELETE FROM variant WHERE optionId = :optionId")
    int deleteOptionVariants(@Bind("optionId") Integer optionId);

    @SqlQuery(value = "select *\n" +
            "from variant\n" +
            "where id = :id;")
    Variant getOptionVariantValueId(@Bind("id") Integer id);


}


