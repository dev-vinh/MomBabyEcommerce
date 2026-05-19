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

    // Load giá trị mẫu cho dropdown. Chỉ lấy optionId IS NULL.
    @SqlQuery("""
            SELECT MIN(v.id) as id,
                   v.categoryId as categoryId,
                   v.name as name,
                   v.value as value,
                   CAST(NULL AS SIGNED) as optionId
            FROM attributes a
            JOIN variant v
                ON v.categoryId = a.categoryId
               AND v.name COLLATE utf8mb4_unicode_ci = a.name COLLATE utf8mb4_unicode_ci
            WHERE a.id = :attributeId
              AND v.optionId IS NULL
              AND v.value IS NOT NULL
              AND TRIM(v.value) <> ''
            GROUP BY v.categoryId, v.name, v.value
            ORDER BY v.value
            """)
    List<Variant> getVariantValuesByAttributeId(@Bind("attributeId") Integer attributeId);

    @SqlQuery("""
            SELECT v.id as id,
                   v.categoryId as categoryId,
                   v.name as name,
                   v.value as value,
                   v.optionId as optionId
            FROM variant v
            WHERE v.id = :id
              AND v.optionId IS NULL
            """)
    Variant getTemplateVariantById(@Bind("id") Integer id);

    @SqlQuery("""
            SELECT COUNT(*)
            FROM attributes a
            JOIN variant v
              ON v.categoryId = a.categoryId
             AND v.name COLLATE utf8mb4_unicode_ci = a.name COLLATE utf8mb4_unicode_ci
            WHERE a.id = :attributeId
              AND v.optionId IS NULL
              AND TRIM(v.value) COLLATE utf8mb4_unicode_ci = TRIM(:value) COLLATE utf8mb4_unicode_ci
              AND (:excludeId IS NULL OR v.id <> :excludeId)
            """)
    int countTemplateValueExists(@Bind("attributeId") Integer attributeId,
                                 @Bind("value") String value,
                                 @Bind("excludeId") Integer excludeId);

    @SqlUpdate("""
            INSERT INTO variant (categoryId, name, value, optionId)
            SELECT a.categoryId, a.name, :value, NULL
            FROM attributes a
            WHERE a.id = :attributeId
            """)
    @GetGeneratedKeys
    int createTemplateValue(@Bind("attributeId") Integer attributeId,
                            @Bind("value") String value);

    @SqlUpdate("""
            UPDATE variant
            SET value = :value
            WHERE id = :id
              AND optionId IS NULL
            """)
    int updateTemplateValue(@Bind("id") Integer id,
                            @Bind("value") String value);

    @SqlUpdate("""
            DELETE FROM variant
            WHERE id = :id
              AND optionId IS NULL
            """)
    int deleteTemplateValue(@Bind("id") Integer id);

    // Tạo biến thể thật cho option sản phẩm từ dòng mẫu dropdown.
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