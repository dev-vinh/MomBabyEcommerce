package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.CategoriesWithStock;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterConstructorMapper(CategoriesWithStock.class)
public interface CategoryCustomDao {

    @SqlQuery("""
    SELECT c.id, c.name, c.isActive,
           COALESCE(SUM(inv.quantity), 0) AS totalStock
    FROM categories c
    LEFT JOIN products p ON c.id = p.categoryId
    LEFT JOIN option_variant o ON p.id = o.productId
    LEFT JOIN inventory inv ON inv.optionVariantId = o.id
    GROUP BY c.id, c.name, c.isActive
""")
    List<CategoriesWithStock> getCustomCategoriesWithStock();


    @SqlQuery("SELECT c.id, c.name FROM categories c WHERE c.name LIKE CONCAT('%', :search, '%')")
    List<CategoriesWithStock> searchCategoriesByName(@Bind("search") String search);
}
