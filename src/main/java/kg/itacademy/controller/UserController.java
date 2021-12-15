package kg.itacademy.controller;

import kg.itacademy.model.user.BaseUserModel;
import kg.itacademy.model.user.UpdateUserModel;
import kg.itacademy.model.user.UserProfileDataModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService USER_SERVICE;

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<BaseUserModel> getById(@PathVariable Long id) {
        return new ResponseMessage<BaseUserModel>()
                .prepareSuccessMessage(USER_SERVICE.getUserModelById(id));
    }

//    @GetMapping("/get-current")
//    public ResponseMessage<BaseUserModel> getCurrentUser() {
//        return new ResponseMessage<BaseUserModel>().prepareSuccessMessage(USER_SERVICE.getCurrentUserModel());
//    }

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