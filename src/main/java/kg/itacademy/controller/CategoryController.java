package kg.itacademy.controller;

import kg.itacademy.entity.Category;
import kg.itacademy.model.ResponseMessage;
import kg.itacademy.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/get-all")
    public ResponseMessage<List<Category>> getAll() {
       return new ResponseMessage<List<Category>>()
                .prepareSuccessMessage(categoryService.getAll());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<Category> getById(@PathVariable Long id) {
        return new ResponseMessage<Category>()
                .prepareSuccessMessage(categoryService.getById(id));
    }
}