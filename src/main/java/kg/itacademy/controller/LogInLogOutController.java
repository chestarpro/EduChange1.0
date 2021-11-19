package kg.itacademy.controller;

import kg.itacademy.entity.User;
import kg.itacademy.model.AuthDataBaseUserModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.model.UserAuthorizModel;
import kg.itacademy.model.UserModel;
import kg.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class LogInLogOutController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public ResponseMessage<UserModel> save(@RequestBody User user) {
        return new ResponseMessage<UserModel>()
                .prepareSuccessMessage(userService.createUser(user));
    }

    @PostMapping("/sign-in")
    public ResponseMessage<AuthDataBaseUserModel> getAuthHeader(@RequestBody UserAuthorizModel userAuthorizModel) {
        AuthDataBaseUserModel authHeader = userService.getBasicAuthorizHeaderByAuthorizModel(userAuthorizModel);
        return new ResponseMessage<AuthDataBaseUserModel>().prepareSuccessMessage(authHeader);
    }
}
