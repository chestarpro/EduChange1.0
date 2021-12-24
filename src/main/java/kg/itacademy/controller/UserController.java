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
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseMessage<UserProfileDataModel> save(@RequestBody CreateUserModel createUserModel) {
        return new ResponseMessage<UserProfileDataModel>()
                .prepareSuccessMessage(userService.createUser(createUserModel));
    }

    @PostMapping("/sign-in")
    public ResponseMessage<UserProfileDataModel> getAuthHeader(@RequestBody UserAuthorizModel userAuthorizModel) {
        UserProfileDataModel authHeader = userService.getBasicAuthorizHeaderByAuthorizModel(userAuthorizModel);
        return new ResponseMessage<UserProfileDataModel>().prepareSuccessMessage(authHeader);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<BaseUserModel> getById(@PathVariable Long id) {
        return new ResponseMessage<BaseUserModel>()
                .prepareSuccessMessage(userService.getUserModelById(id));
    }

    @GetMapping("/get-updated-profile-by-id/{id}")
    public ResponseMessage<UserProfileDataModel> getUpdateUserProfileById(@PathVariable Long id) {
        return new ResponseMessage<UserProfileDataModel>()
                .prepareSuccessMessage(userService.getUserProfileDataModelById(id));
    }

    @PutMapping("/update")
    public ResponseMessage<UserProfileDataModel> update(@RequestBody UpdateUserModel updateUserModel) {
        return new ResponseMessage<UserProfileDataModel>()
                .prepareSuccessMessage(userService.updateUser(updateUserModel));
    }

    @DeleteMapping("/delete")
    public ResponseMessage<BaseUserModel> delete() {
        return new ResponseMessage<BaseUserModel>()
                .prepareSuccessMessage(userService.deleteUser());
    }
}