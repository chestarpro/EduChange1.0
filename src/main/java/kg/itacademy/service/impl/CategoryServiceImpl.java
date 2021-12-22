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
    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public CategoryModel createCategory(String categoryName) {
        validateCategoryName(categoryName);
        Category category = categoryRepository.findByCategoryName(categoryName).orElse(null);

        if (category != null)
            throw new ApiFailException("Категория " + categoryName + " уже существует");

        category = new Category();
        category.setCategoryName(categoryName.toLowerCase(Locale.ROOT));
        save(category);
        return categoryConverter.convertFromEntity(category);
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public CategoryModel getCategoryModelById(Long id) {
        return categoryConverter.convertFromEntity(getById(id));
    }

    @Override
    public CategoryModel getByCategoryName(String categoryName) {
        return categoryConverter
                .convertFromEntity(categoryRepository
                        .findByCategoryName(categoryName.toLowerCase(Locale.ROOT))
                        .orElse(null));
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<CategoryModel> getAllCategoryModel() {
        return getAll()
                .stream()
                .map(categoryConverter::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryModel updateCategory(CategoryModel categoryModel) {
        Long categoryId = categoryModel.getId();

        if (categoryId == null)
            throw new ApiFailException("Не указан ID категории");

        Category dataCategory = getById(categoryId);

        if (dataCategory == null)
            throw new ApiFailException("Категория под ID " + categoryId + " не найдена");

        String updateCategoryName = categoryModel.getCategoryName();
        validateCategoryName(updateCategoryName);
        dataCategory.setCategoryName(updateCategoryName.toLowerCase(Locale.ROOT));
        categoryRepository.save(dataCategory);
        return categoryModel;
    }

    private void validateCategoryName(String categoryName) {
        if (categoryName == null || categoryName.isEmpty())
            throw new ApiFailException("Название категории не заполнено");
        if (categoryName.length() > 50)
            throw new ApiFailException("Длинна символов ограниченно(50)");
    }
}