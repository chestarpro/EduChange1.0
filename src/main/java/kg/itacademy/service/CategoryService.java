package kg.itacademy.service;

import kg.itacademy.entity.Category;
import kg.itacademy.model.category.CategoryModel;

import java.util.List;

public interface CategoryService extends BaseService<Category> {
    List<CategoryModel> getAllCategoryModel();

    CategoryModel updateCategory(CategoryModel categoryModel);

    CategoryModel getByCategoryName(String categoryName);

    CategoryModel createCategory(String categoryName);

    CategoryModel getCategoryModelById(Long id);

    CategoryModel deleteCategory(Long id);
}
