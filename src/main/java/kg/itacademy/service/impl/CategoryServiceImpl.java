package kg.itacademy.service.impl;

import kg.itacademy.converter.CategoryConverter;
import kg.itacademy.entity.Category;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.CategoryModel;
import kg.itacademy.repository.CategoryRepository;
import kg.itacademy.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        if (category.getCategoryName() == null || category.getCategoryName().isEmpty())
            throw new ApiFailException("Не указан название категории");

        Category dataCategory = getByCategoryName(category.getCategoryName());
        if (dataCategory != null)
            throw new ApiFailException("Категория " + dataCategory.getCategoryName() + " уже существует");

        return categoryRepository.save(category);
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public CategoryModel getCategoryModelById(Long id) {
        Category dataCategory = getById(id);
        if (dataCategory == null)
            throw new ApiFailException("Категории по id " + id + " не найдено");
        return new CategoryConverter().convertFromEntity(dataCategory);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category update(Category category) {
        if (category.getId() == null)
            throw new IllegalArgumentException("Не указан id категории");
        if (category.getCategoryName() != null && category.getCategoryName().isEmpty())
            throw new ApiFailException("Не указан название категории");
        return categoryRepository.save(category);
    }

    @Override
    public Category getByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).orElse(null);
    }

    @Override
    public Category deleteCategory(Long id) {
        Category category = getById(id);
        categoryRepository.delete(category);
        return category;
    }
}