package kg.itacademy.service.impl;

import kg.itacademy.converter.CategoryConverter;
import kg.itacademy.entity.Category;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.CategoryModel;
import kg.itacademy.repository.CategoryRepository;
import kg.itacademy.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository CATEGORY_REPOSITORY;
    private final CategoryConverter CATEGORY_CONVERTER;

    @Override
    public Category save(Category category) {
        return CATEGORY_REPOSITORY.save(category);
    }

    @Override
    public CategoryModel createCategory(String categoryName) {
        validateCategoryName(categoryName);
        Category category = CATEGORY_REPOSITORY.findByCategoryName(categoryName).orElse(null);

        if (category != null)
            throw new ApiFailException("Category " + categoryName + " already exists");

        category = new Category();
        category.setCategoryName(categoryName.toLowerCase(Locale.ROOT));
        save(category);

        return CATEGORY_CONVERTER.convertFromEntity(category);
    }

    @Override
    public Category getById(Long id) {
        return CATEGORY_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public CategoryModel getCategoryModelById(Long id) {
        return CATEGORY_CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public CategoryModel getByCategoryName(String categoryName) {
        return CATEGORY_CONVERTER
                .convertFromEntity(CATEGORY_REPOSITORY
                        .findByCategoryName(categoryName.toLowerCase(Locale.ROOT))
                        .orElse(null));
    }

    @Override
    public List<Category> getAll() {
        return CATEGORY_REPOSITORY.findAll();
    }

    @Override
    public List<CategoryModel> getAllCategoryModel() {
        return getAll()
                .stream()
                .map(CATEGORY_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryModel updateCategory(CategoryModel categoryModel) {
        Long categoryId = categoryModel.getId();

        if (categoryId == null)
            throw new ApiFailException("Category id is not specified");

        Category dataCategory = getById(categoryId);

        if (dataCategory == null)
            throw new ApiFailException("Category by id " + categoryId + " not found");

        String updateCategoryName = categoryModel.getCategoryName();
        validateCategoryName(updateCategoryName);
        dataCategory.setCategoryName(updateCategoryName.toLowerCase(Locale.ROOT));
        CATEGORY_REPOSITORY.save(dataCategory);
        return categoryModel;
    }

    private void validateCategoryName(String categoryName) {
        if (categoryName == null || categoryName.isEmpty())
            throw new ApiFailException("Category name is not filled");
        if (categoryName.length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for category name");
    }
}