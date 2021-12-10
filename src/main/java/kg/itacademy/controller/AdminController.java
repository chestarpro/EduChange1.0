package kg.itacademy.controller;

import kg.itacademy.aop.LogMethod;
import kg.itacademy.model.*;
import kg.itacademy.service.*;
import kg.itacademy.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserLogService userLogService;

    @Autowired
    private UserBalanceService userBalanceService;

    @Autowired
    private UserImageService userImageService;

    @Autowired
    private CourseImageService courseImageService;

    @Autowired
    private UserCourseMappingService userCourseMappingService;

    @PostMapping("/category/create/{categoryName}")
    public ResponseMessage<CategoryModel> saveCategory(@PathVariable String categoryName) {
        return new ResponseMessage<CategoryModel>()
                .prepareSuccessMessage(categoryService.createCategory(categoryName));
    }

    @PutMapping("/category/update")
    public ResponseMessage<CategoryModel> updateCategory(@RequestBody CategoryModel categoryModel) {
        return new ResponseMessage<CategoryModel>()
                .prepareSuccessMessage(categoryService.updateCategory(categoryModel));
    }

    @GetMapping("/user/get-all")
    @LogMethod
    public ResponseMessage<List<UserModel>> getAllUser() {
        return new ResponseMessage<List<UserModel>>()
                .prepareSuccessMessage(userService.getAllUserModels());
    }

    @GetMapping("/comment/get-all")
    public ResponseMessage<List<CommentModel>> getAllComment() {
        return new ResponseMessage<List<CommentModel>>()
                .prepareSuccessMessage(commentService.getAllCommentModel());
    }

    @GetMapping("/user-log/get-all/by-user-id/{userId}")
    public ResponseMessage<List<UserLogModel>> getAllUserLogByUserId(@PathVariable Long userId) {
        return new ResponseMessage<List<UserLogModel>>()
                .prepareSuccessMessage(userLogService.getAllByUserId(userId));
    }

    @GetMapping("user-balance/get-all")
    public ResponseMessage<List<UserBalanceModel>> getAllUserBalanceModel() {
        return new ResponseMessage<List<UserBalanceModel>>()
                .prepareSuccessMessage(userBalanceService.getAllUserBalanceModel());
    }

    @GetMapping("/user-image/get-all")
    public ResponseMessage<List<UserImageModel>> getAllUserImageModel() {
        return new ResponseMessage<List<UserImageModel>>()
                .prepareSuccessMessage(userImageService.getAllUserImageModel());
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseMessage<UserModel> deleteUser(@PathVariable Long id) {
        return new ResponseMessage<UserModel>()
                .prepareSuccessMessage(userService.deleteUserByAdmin(id));
    }

    @DeleteMapping("/category/delete/{id}")
    public ResponseMessage<CategoryModel> deleteCategory(@PathVariable Long id) {
        return new ResponseMessage<CategoryModel>()
                .prepareSuccessMessage(categoryService.deleteCategory(id));
    }

    @DeleteMapping("/purchases/delete/{id}")
    public ResponseMessage<UserCourseMappingModel> delete(@PathVariable Long id) {
        return new ResponseMessage<UserCourseMappingModel>()
                .prepareSuccessMessage(userCourseMappingService.deleteMapping(id));
    }
}