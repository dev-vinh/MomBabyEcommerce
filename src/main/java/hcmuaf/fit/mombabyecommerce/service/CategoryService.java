package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.CategoryDao;
import hcmuaf.fit.mombabyecommerce.model.Category;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class CategoryService {
    private final CategoryDao categoryDao;

    public CategoryService(Jdbi jdbi) {
        this.categoryDao = jdbi.onDemand(CategoryDao.class);
    }




    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }



    public Category getCategoryById(Integer id) {
        Category category = categoryDao.getCategoryById(id);
        if (category == null) {
            throw new IllegalArgumentException("Category not found");
        }
        return category;
    }


    public List<Category> getTop7Categories() {
        return categoryDao.getTop7Categories();
    }
    public Category createCategory(String name, Boolean isActive ) {
        int id = categoryDao.createCategory(name, isActive);
        return categoryDao.getCategoryById(id);
    }

    public void updateCategory(Integer id, String name) {
        categoryDao.updateCategory(id, name);
    }

    public void deleteCategory(Integer id) {
        categoryDao.deleteCategory(id);
    }


}
