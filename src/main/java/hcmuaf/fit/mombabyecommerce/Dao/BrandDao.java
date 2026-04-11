package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Brand;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterConstructorMapper(Brand.class)
public interface BrandDao {
    @SqlQuery("SELECT * FROM brands WHERE isActive = 1")
    List<Brand> getAllBrand();

    @SqlQuery("SELECT * FROM brands WHERE id = :id")
    Brand getBrandById(@Bind("id") Integer id);

    @SqlUpdate("INSERT INTO brands (name) VALUES (:name)")
    @GetGeneratedKeys("id")
    int createBrand(@Bind("name") String name);

    @SqlUpdate("UPDATE brands SET name = :name WHERE id = :id")
    void updateBrand(@Bind("id") Integer id, @Bind("name") String name);

    @SqlUpdate("DELETE FROM brands WHERE id = :id")
    void deleteBrand(@Bind("id") Integer id);



}
