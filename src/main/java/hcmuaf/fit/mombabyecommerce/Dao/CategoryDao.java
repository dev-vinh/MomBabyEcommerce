package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Category;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterConstructorMapper(Category.class)
public interface CategoryDao {

    @SqlQuery("SELECT * FROM categories WHERE isActive = 1")
    List<Category> getAllCategories();

    @SqlQuery("""
    SELECT * FROM categories WHERE isActive = 1 AND isFeatured = 1
    ORDER BY displayOrder ASC
    LIMIT 7
""")
    List<Category> getTop7Categories();

    @SqlQuery("SELECT * FROM categories WHERE id = :id")
    Category getCategoryById(@Bind("id") Integer id);

    @SqlUpdate("INSERT INTO categories (name, isActive) VALUES (:name, COALESCE(:isActive, 1))")
    @GetGeneratedKeys("id")
    int createCategory(@Bind("name") String name, @Bind("isActive") Boolean isActive);


    @SqlUpdate("UPDATE categories SET name = :name WHERE id = :id")
    void updateCategory(@Bind("id") Integer id, @Bind("name") String name);

    @SqlUpdate("DELETE FROM categories WHERE id = :id")
    void deleteCategory(@Bind("id") Integer id);



}
