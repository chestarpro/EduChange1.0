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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryConverter CONVERTER = new CategoryConverter();

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
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

        return CONVERTER.convertFromEntity(save(Category.builder().categoryName(categoryName).build()));
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public CategoryModel getCategoryModelById(Long id) {
        return CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<CategoryModel> getAllCategoryModel() {
        return getAll()
                .stream()
                .map(CONVERTER::convertFromEntity)
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

        return CONVERTER.convertFromEntity(categoryRepository.save(CONVERTER.convertFromModel(categoryModel)));
    }

    @Override
    public CategoryModel getByCategoryName(String categoryName) {
        return CONVERTER.convertFromEntity(categoryRepository.findByCategoryName(categoryName).orElse(null));
    }

    @Override
    public CategoryModel deleteCategory(Long id) {
        Category category = getById(id);
        if (category == null) {
            throw new ApiFailException("Category by id " + id + " not found");
        }
        categoryRepository.delete(category);
        return CONVERTER.convertFromEntity(category);
    }
}