package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Variant;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterConstructorMapper(Variant.class)
public interface VariantDao {

    @SqlQuery("""
            SELECT a.id as id,
                   a.categoryId as categoryId,
                   a.name as name,
                   CAST(NULL AS CHAR) as value,
                   CAST(NULL AS SIGNED) as optionId
            FROM attributes a
            WHERE (:categoryId IS NULL OR a.categoryId = :categoryId)
            ORDER BY a.name
            """)
    List<Variant> getVariantsByCategoryId(@Bind("categoryId") Integer categoryId);


    @SqlQuery("""
            SELECT MIN(v.id) as id,
                   v.categoryId as categoryId,
                   v.name as name,
                   v.value as value,
                   CAST(NULL AS SIGNED) as optionId
            FROM attributes a
            JOIN variant v 
                ON v.categoryId = a.categoryId
               AND v.name = a.name
            LEFT JOIN option_variant ov 
                ON ov.id = v.optionId
            WHERE a.id = :attributeId
              AND v.value IS NOT NULL
              AND TRIM(v.value) <> ''
              AND (ov.id IS NULL OR ov.isActive = 1)
            GROUP BY v.categoryId, v.name, v.value
            ORDER BY v.value
            """)
    List<Variant> getVariantValuesByAttributeId(@Bind("attributeId") Integer attributeId);



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
}