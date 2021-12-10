package kg.itacademy.controller;

import kg.itacademy.entity.User;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.model.user.UserModel;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService USER_SERVICE;

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<UserModel> getById(@PathVariable Long id) {
        return new ResponseMessage<UserModel>()
                .prepareSuccessMessage(USER_SERVICE.getUserModelById(id));
    }

    @GetMapping("/get-current")
    public ResponseMessage<UserModel> getCurrentUser() {
        return new ResponseMessage<UserModel>().prepareSuccessMessage(USER_SERVICE.getCurrentUserModel());
    }

    @PutMapping("/update")
    public ResponseMessage<UserModel> update(@RequestBody User user) {
        return new ResponseMessage<UserModel>()
                .prepareSuccessMessage(USER_SERVICE.updateUser(user));
    }

    @DeleteMapping("/delete")
    public ResponseMessage<UserModel> delete() {
        return new ResponseMessage<UserModel>()
                .prepareSuccessMessage(USER_SERVICE.deleteUser());
    }
}