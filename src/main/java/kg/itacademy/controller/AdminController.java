package kg.itacademy.controller;

import kg.itacademy.model.CategoryModel;
import kg.itacademy.model.UserLogModel;
import kg.itacademy.model.user.BaseUserModel;
import kg.itacademy.model.userImage.UserImageModel;
import kg.itacademy.service.*;
import kg.itacademy.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final UserLogService userLogService;
    private final UserImageService userImageService;
    private final UserCourseMappingService userCourseMappingService;

    @PostMapping("/category/create/{categoryName}")
    public ResponseMessage<CategoryModel> createCategory(@PathVariable String categoryName) {
        return new ResponseMessage<CategoryModel>()
                .prepareSuccessMessage(categoryService.createCategory(categoryName));
    }

    @PutMapping("/category/update")
    public ResponseMessage<CategoryModel> updateCategory(@RequestBody CategoryModel categoryModel) {
        return new ResponseMessage<CategoryModel>()
                .prepareSuccessMessage(categoryService.updateCategory(categoryModel));
    }

    @GetMapping("/user/get-all")
    public ResponseMessage<List<BaseUserModel>> getAllUser() {
        return new ResponseMessage<List<BaseUserModel>>()
                .prepareSuccessMessage(userService.getAllUserModels());
    }

    @GetMapping("/user-log/get-all/by-user-id/{userId}")
    public ResponseMessage<List<UserLogModel>> getAllUserLogByUserId(@PathVariable Long userId) {
        return new ResponseMessage<List<UserLogModel>>()
                .prepareSuccessMessage(userLogService.getAllByUserId(userId));
    }

    @GetMapping("/user-image/get-all")
    public ResponseMessage<List<UserImageModel>> getAllUserImageModel() {
        return new ResponseMessage<List<UserImageModel>>()
                .prepareSuccessMessage(userImageService.getAllUserImageModel());
    }

    @DeleteMapping("/user/delete/{userId}")
    public ResponseMessage<BaseUserModel> deleteUser(@PathVariable Long userId) {
        return new ResponseMessage<BaseUserModel>()
                .prepareSuccessMessage(userService.deleteUserByAdmin(userId));
    }
}