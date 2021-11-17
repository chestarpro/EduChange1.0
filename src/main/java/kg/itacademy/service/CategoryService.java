package kg.itacademy.service;

import kg.itacademy.entity.Category;
import kg.itacademy.model.CategoryModel;

public interface CategoryService extends BaseService<Category> {
    Category getByCategoryName(String categoryName);
    CategoryModel getCategoryModelById(Long id);
    Category deleteCategory(Long id);
}
