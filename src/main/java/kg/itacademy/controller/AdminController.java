package kg.itacademy.controller;

import kg.itacademy.model.*;
import kg.itacademy.model.CategoryModel;
import kg.itacademy.model.user.BaseUserModel;
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
    private final UserService USER_SERVICE;
    private final CategoryService CATEGORY_SERVICE;
    private final UserLogService USER_LOG_SERVICE;
    private final UserImageService USER_IMAGE_SERVICE;
    private final UserCourseMappingService USER_COURSE_MAPPING_SERVICE;

    @PostMapping("/category/create/{categoryName}")
    public ResponseMessage<CategoryModel> saveCategory(@PathVariable String categoryName) {
        return new ResponseMessage<CategoryModel>()
                .prepareSuccessMessage(CATEGORY_SERVICE.createCategory(categoryName));
    }

    @PutMapping("/category/update")
    public ResponseMessage<CategoryModel> updateCategory(@RequestBody CategoryModel categoryModel) {
        return new ResponseMessage<CategoryModel>()
                .prepareSuccessMessage(CATEGORY_SERVICE.updateCategory(categoryModel));
    }

    @GetMapping("/user/get-all")
    public ResponseMessage<List<BaseUserModel>> getAllUser() {
        return new ResponseMessage<List<BaseUserModel>>()
                .prepareSuccessMessage(USER_SERVICE.getAllUserModels());
    }

    @GetMapping("/user-log/get-all/by-user-id/{userId}")
    public ResponseMessage<List<UserLogModel>> getAllUserLogByUserId(@PathVariable Long userId) {
        return new ResponseMessage<List<UserLogModel>>()
                .prepareSuccessMessage(USER_LOG_SERVICE.getAllByUserId(userId));
    }

    @GetMapping("/user-image/get-all")
    public ResponseMessage<List<UserImageModel>> getAllUserImageModel() {
        return new ResponseMessage<List<UserImageModel>>()
                .prepareSuccessMessage(USER_IMAGE_SERVICE.getAllUserImageModel());
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseMessage<BaseUserModel> deleteUser(@PathVariable Long id) {
        return new ResponseMessage<BaseUserModel>()
                .prepareSuccessMessage(USER_SERVICE.deleteUserByAdmin(id));
    }

    @DeleteMapping("/purchases/delete/{id}")
    public ResponseMessage<UserCourseMappingModel> delete(@PathVariable Long id) {
        return new ResponseMessage<UserCourseMappingModel>()
                .prepareSuccessMessage(USER_COURSE_MAPPING_SERVICE.deleteMapping(id));
    }
}