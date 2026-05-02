package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.BannerDao;
import hcmuaf.fit.mombabyecommerce.model.Banner;
import org.jdbi.v3.core.Jdbi;

import java.time.LocalDate;
import java.util.List;

public class BannerService {
    private final BannerDao bannerDAO;
    public BannerService(Jdbi jdbi) {
        this.bannerDAO = jdbi.onDemand(BannerDao.class);
    }

    public List<Banner> getAllBanners() {
        return bannerDAO.getAllBanners();
    }

    public Banner getBannerById(int id) {
        return bannerDAO.getBannerById(id);
    }

    public Banner createBanner(String imageId, LocalDate startDate, LocalDate endDate, boolean isActive) {
        int id = bannerDAO.createBanner(imageId, startDate, endDate, isActive);
        return getBannerById(id);
    }

    public void updateBanner(int id, String imageId, LocalDate startDate, LocalDate endDate) {
        bannerDAO.updateBanner(id, imageId, startDate, endDate);
    }

    public void toggleBannerStatus(int id, boolean isActive) {
        bannerDAO.updateBannerTitle(id, isActive);
    }


    public void deleteBanner(int id) {
        bannerDAO.deleteBanner(id);
    }
}
