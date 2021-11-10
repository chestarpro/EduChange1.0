package kg.itacademy.service;

import kg.itacademy.entity.Category;

public interface CategoryService extends BaseService<Category> {
    Category getByCategoryName(String categoryName);
    Category deleteCategory(Long id);
}
