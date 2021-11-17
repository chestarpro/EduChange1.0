package kg.itacademy.controller;

import kg.itacademy.entity.User;
import kg.itacademy.model.ResponseMessage;
import kg.itacademy.model.UserAuthorizModel;
import kg.itacademy.model.UserModel;
import kg.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up/")
    public ResponseMessage<UserModel> save(@RequestBody User user) {
        return new ResponseMessage<UserModel>()
                .prepareSuccessMessage(userService.createUser(user));
    }

    @PostMapping("/sign-in")
    public ResponseMessage<String> getAuthHeader(@RequestBody UserAuthorizModel userAuthorizModel) {
        String authHeader = userService.getBasicAuthorizHeaderByAuthorizModel(userAuthorizModel);
        return new ResponseMessage<String>().prepareSuccessMessage(authHeader);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<UserModel> getById(@PathVariable Long id) {
        return new ResponseMessage<UserModel>()
                .prepareSuccessMessage(userService.getUserModelById(id));
    }

    @GetMapping("/get-current")
    public ResponseMessage<UserModel> getCurrentUser() {
        return new ResponseMessage<UserModel>().prepareSuccessMessage(userService.getCurrentUserModel());
    }

    @PutMapping("/update")
    public ResponseMessage<UserModel> update(@RequestBody User user) {
        return new ResponseMessage<UserModel>()
                .prepareSuccessMessage(userService.updateUser(user));
    }

    @DeleteMapping("/delete")
    public ResponseMessage<UserModel> delete() {
        return new ResponseMessage<UserModel>()
                .prepareSuccessMessage(userService.deleteUser());
    }
}