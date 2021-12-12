package kg.itacademy.service;

import kg.itacademy.entity.Category;
import kg.itacademy.model.category.CategoryModel;

import java.util.List;

public interface CategoryService extends BaseService<Category> {
    CategoryModel createCategory(String categoryName);

    CategoryModel updateCategory(CategoryModel categoryModel);

    CategoryModel getCategoryModelById(Long id);

    CategoryModel getByCategoryName(String categoryName);

    List<CategoryModel> getAllCategoryModel();
}