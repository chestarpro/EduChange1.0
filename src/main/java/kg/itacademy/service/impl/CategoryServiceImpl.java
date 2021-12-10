package kg.itacademy.service.impl;

import kg.itacademy.converter.CategoryConverter;
import kg.itacademy.entity.Category;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.course.CategoryModel;
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
        if (categoryName == null || categoryName.isEmpty())
            throw new ApiFailException("Не указан название категории");

        if (categoryName.length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for category name");

        CategoryModel dataCategory = getByCategoryName(categoryName);
        if (dataCategory != null)
            throw new ApiFailException("Категория " + dataCategory.getCategoryName() + " уже существует");

        return CATEGORY_CONVERTER.convertFromEntity(save(Category.builder()
                .categoryName(categoryName.toLowerCase(Locale.ROOT))
                .build()));
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
        if (categoryModel.getId() == null)
            throw new IllegalArgumentException("Не указан id категории");

        if (categoryModel.getCategoryName() == null || categoryModel.getCategoryName().isEmpty())
            throw new ApiFailException("Не указан название категории");

        if (categoryModel.getCategoryName().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for category name");

        return CATEGORY_CONVERTER.convertFromEntity(CATEGORY_REPOSITORY.save(CATEGORY_CONVERTER.convertFromModel(categoryModel)));
    }

    @Override
    public CategoryModel getByCategoryName(String categoryName) {
        return CATEGORY_CONVERTER.convertFromEntity(CATEGORY_REPOSITORY.findByCategoryName(categoryName).orElse(null));
    }

    @Override
    public CategoryModel deleteCategory(Long id) {
        Category category = getById(id);
        if (category == null) {
            throw new ApiFailException("Category by id " + id + " not found");
        }
        CATEGORY_REPOSITORY.delete(category);
        return CATEGORY_CONVERTER.convertFromEntity(category);
    }
}