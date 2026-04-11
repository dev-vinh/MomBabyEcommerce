package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Category;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterConstructorMapper(Category.class)
public interface CategoryRepository {
    @SqlUpdate("INSERT INTO categories (name) VALUES (:name)")
    void addCategory(@Bind("name") String name);
}
