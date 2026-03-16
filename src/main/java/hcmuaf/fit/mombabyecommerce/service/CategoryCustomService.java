package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.CategoryCustomDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.CategoriesWithStock;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class CategoryCustomService {
    private final CategoryCustomDao categoryCustomDAO;

    public CategoryCustomService(Jdbi jdbi) {
        this.categoryCustomDAO = jdbi.onDemand(CategoryCustomDao.class);
    }

    public List<CategoriesWithStock> getCustomCategoriesWithTotalStock() {
        return categoryCustomDAO.getCustomCategoriesWithStock();
    }

    public List<CategoriesWithStock> searchCategories(String search) {
        return categoryCustomDAO.searchCategoriesByName(search);
    }


    public static void main(String[] args) {
        CategoryCustomService categoryCustomService =  new CategoryCustomService(DBConnection.getJdbi());
        List<CategoriesWithStock> categories = categoryCustomService.getCustomCategoriesWithTotalStock();
        System.out.println(categories);
    }
}
