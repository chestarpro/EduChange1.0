package kg.itacademy.controller;

import kg.itacademy.model.user.CreateUserModel;
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
@RequestMapping("/sign")
@RequiredArgsConstructor
public class LogInLogOutController {
    private final UserService USER_SERVICE;

    @PostMapping("/up")
    public ResponseMessage<UserProfileDataModel> save(@RequestBody CreateUserModel createUserModel) {
        return new ResponseMessage<UserProfileDataModel>()
                .prepareSuccessMessage(USER_SERVICE.createUser(createUserModel));
    }

    @PostMapping("/in")
    public ResponseMessage<UserProfileDataModel> getAuthHeader(@RequestBody UserAuthorizModel userAuthorizModel) {
        UserProfileDataModel authHeader = USER_SERVICE.getBasicAuthorizHeaderByAuthorizModel(userAuthorizModel);
        return new ResponseMessage<UserProfileDataModel>().prepareSuccessMessage(authHeader);
    }
}
