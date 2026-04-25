package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.CategoryRepository;
import hcmuaf.fit.mombabyecommerce.model.Category;
import org.jdbi.v3.core.Jdbi;

public class CategoryManager {
    private final CategoryRepository categoryRepository;

    public CategoryManager(Jdbi jdbi) {
        this.categoryRepository = jdbi.onDemand(CategoryRepository.class);
    }

    public void addCategory(Category category) {
        categoryRepository.addCategory(category.getName());
    }
}
