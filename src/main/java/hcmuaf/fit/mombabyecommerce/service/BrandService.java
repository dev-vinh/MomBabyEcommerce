package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.BrandDao;
import hcmuaf.fit.mombabyecommerce.model.Brand;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class BrandService {
    private final BrandDao brandDao;

    public BrandService(Jdbi jdbi) {
        this.brandDao = jdbi.onDemand(BrandDao.class);
    }

    public List<Brand> getAllBrands() {
        return brandDao.getAllBrand();
    }
    public List<Brand> getBrandsByCategory(Integer categoryId) {
        return brandDao.getBrandsByCategory(categoryId);
    }

    public Brand getBrandById(Integer id) {
        return brandDao.getBrandById(id);
    }

    public Brand createBrand(String name) {
        int id = brandDao.createBrand(name);
        return brandDao.getBrandById(id);
    }

    public void updateBrand(Integer id, String name) {
        brandDao.updateBrand(id, name);
    }

    public void deleteBrand(Integer id) {
        brandDao.deleteBrand(id);
    }
}

