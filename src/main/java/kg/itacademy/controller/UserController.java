package kg.itacademy.controller;

import kg.itacademy.model.user.*;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService USER_SERVICE;

    @PostMapping("/sign-up")
    public ResponseMessage<UserProfileDataModel> save(@RequestBody CreateUserModel createUserModel) {
        return new ResponseMessage<UserProfileDataModel>()
                .prepareSuccessMessage(USER_SERVICE.createUser(createUserModel));
    }

    @PostMapping("/sign-in")
    public ResponseMessage<UserProfileDataModel> getAuthHeader(@RequestBody UserAuthorizModel userAuthorizModel) {
        UserProfileDataModel authHeader = USER_SERVICE.getBasicAuthorizHeaderByAuthorizModel(userAuthorizModel);
        return new ResponseMessage<UserProfileDataModel>().prepareSuccessMessage(authHeader);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<BaseUserModel> getById(@PathVariable Long id) {
        return new ResponseMessage<BaseUserModel>()
                .prepareSuccessMessage(USER_SERVICE.getUserModelById(id));
    }

    @PutMapping("/update")
    public ResponseMessage<UserProfileDataModel> update(@RequestBody UpdateUserModel updateUserModel) {
        return new ResponseMessage<UserProfileDataModel>()
                .prepareSuccessMessage(USER_SERVICE.updateUser(updateUserModel));
    }

    @DeleteMapping("/delete")
    public ResponseMessage<BaseUserModel> delete() {
        return new ResponseMessage<BaseUserModel>()
                .prepareSuccessMessage(USER_SERVICE.deleteUser());
    }
}