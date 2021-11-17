package kg.itacademy.controller;

import kg.itacademy.entity.Category;
import kg.itacademy.model.*;
import kg.itacademy.service.*;
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

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserLogService userLogService;

    @Autowired
    private UserBalanceService userBalanceService;

    @Autowired
    private UserImageService userImageService;

    @PostMapping("/category/create")
    public ResponseMessage<Category> saveCategory(@RequestBody Category category) {
        return new ResponseMessage<Category>()
                .prepareSuccessMessage(categoryService.save(category));
    }

    @PutMapping("/category/update")
    public ResponseMessage<Category> updateCategory(@RequestBody Category category) {
        return new ResponseMessage<Category>()
                .prepareSuccessMessage(categoryService.update(category));
    }

    @DeleteMapping("/category/delete/{id}")
    public ResponseMessage<Category> deleteCategory(@PathVariable Long id) {
        return new ResponseMessage<Category>()
                .prepareSuccessMessage(categoryService.deleteCategory(id));
    }

    @GetMapping("/user/get-all")
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

    @GetMapping("user-balance/get-all/")
    public ResponseMessage<List<UserBalanceModel>> getAllUserBalanceModel() {
        return new ResponseMessage<List<UserBalanceModel>>()
                .prepareSuccessMessage(userBalanceService.getAllUserBalanceModel());
    }

    @GetMapping("")
    public ResponseMessage<List<UserImageModel>> getAllUserImageModel() {
        return new ResponseMessage<List<UserImageModel>>()
                .prepareSuccessMessage(userImageService.getAllUserImageModel());
    }
}