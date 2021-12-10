package kg.itacademy.controller;

import kg.itacademy.entity.User;
import kg.itacademy.model.user.UserProfileDataModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.model.user.UserAuthorizModel;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class LogInLogOutController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseMessage<UserProfileDataModel> save(@RequestBody User user) {
        return new ResponseMessage<UserProfileDataModel>()
                .prepareSuccessMessage(userService.createUser(user));
    }

    @PostMapping("/sign-in")
    public ResponseMessage<UserProfileDataModel> getAuthHeader(@RequestBody UserAuthorizModel userAuthorizModel) {
        UserProfileDataModel authHeader = userService.getBasicAuthorizHeaderByAuthorizModel(userAuthorizModel);
        return new ResponseMessage<UserProfileDataModel>().prepareSuccessMessage(authHeader);
    }
}
