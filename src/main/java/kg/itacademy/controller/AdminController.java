package kg.itacademy.controller;

import kg.itacademy.converter.CategoryConverter;
import kg.itacademy.entity.Category;
import kg.itacademy.model.CategoryModel;
import kg.itacademy.model.ResponseMessage;
import kg.itacademy.model.UserModel;
import kg.itacademy.service.CategoryService;
import kg.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create-category")
    public ResponseMessage<CategoryModel> saveCategory(@RequestBody CategoryModel categoryModel) {
        Category category = new CategoryConverter().convertFromModel(categoryModel);
        return new ResponseMessage<CategoryModel>()
                .prepareSuccessMessage(new CategoryConverter().convertFromEntity(categoryService.save(category)));
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<UserModel>> getAllUser() {
        return new ResponseMessage<List<UserModel>>().prepareSuccessMessage(userService.getAllUserModels());
    }
}