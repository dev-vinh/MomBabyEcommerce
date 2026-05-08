package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Banner;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;
import java.util.List;

public interface BannerDao {
    @SqlQuery("SELECT * FROM banners")
    List<Banner> getAllBanners();

    @SqlQuery("SELECT * FROM banners WHERE id = :id")
    Banner getBannerById(@Bind("id") Integer id);

    @SqlUpdate("INSERT INTO banners (imageId, startDate, endDate, isActive) " +
            "VALUES (:imageId, :startDate, :endDate, :isActive)")
    @GetGeneratedKeys("id")
    int createBanner(@Bind("imageId") String imageId,
                     @Bind("startDate") LocalDate startDate,
                     @Bind("endDate") LocalDate endDate,
                     @Bind("isActive") boolean isActive);


    @SqlUpdate("UPDATE banners SET imageId = :imageId, startDate = :startDate, endDate = :endDate WHERE id = :id")
    void updateBanner(@Bind("id") Integer id,
                      @Bind("imageId") String imageId,
                      @Bind("startDate") LocalDate startDate,
                      @Bind("endDate") LocalDate endDate);


    @SqlUpdate("DELETE FROM banners WHERE id = :id")
    void deleteBanner(@Bind("id") Integer id);

    @SqlUpdate("UPDATE banners SET isActive = :isActive WHERE id = :id")
    void updateBannerTitle(@Bind("id") Integer id, @Bind("isActive") boolean isActive);

}
