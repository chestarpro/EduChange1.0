package kg.itacademy.controller;

import kg.itacademy.model.*;
import kg.itacademy.model.category.CategoryModel;
import kg.itacademy.model.balance.UserBalanceModel;
import kg.itacademy.model.user.BaseUser;
import kg.itacademy.model.userImage.UserImageModel;
import kg.itacademy.model.UserLogModel;
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
    private final CommentService commentService;
    private final UserLogService userLogService;
    private final UserBalanceService userBalanceService;
    private final UserImageService userImageService;
    private final UserCourseMappingService userCourseMappingService;

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
    public ResponseMessage<List<BaseUser>> getAllUser() {
        return new ResponseMessage<List<BaseUser>>()
                .prepareSuccessMessage(userService.getAllUserModels());
    }

    @GetMapping("/user-log/get-all/by-user-id/{userId}")
    public ResponseMessage<List<UserLogModel>> getAllUserLogByUserId(@PathVariable Long userId) {
        return new ResponseMessage<List<UserLogModel>>()
                .prepareSuccessMessage(userLogService.getAllByUserId(userId));
    }

    @GetMapping("/user-balance/get-all")
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
    public ResponseMessage<BaseUser> deleteUser(@PathVariable Long id) {
        return new ResponseMessage<BaseUser>()
                .prepareSuccessMessage(userService.deleteUserByAdmin(id));
    }

    @DeleteMapping("/purchases/delete/{id}")
    public ResponseMessage<UserCourseMappingModel> delete(@PathVariable Long id) {
        return new ResponseMessage<UserCourseMappingModel>()
                .prepareSuccessMessage(userCourseMappingService.deleteMapping(id));
    }
}