package kg.itacademy.controller;

import kg.itacademy.converter.UserConverter;
import kg.itacademy.entity.User;
import kg.itacademy.model.ResponseMessage;
import kg.itacademy.model.UserAuthorizModel;
import kg.itacademy.model.UserModel;
import kg.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public ResponseMessage<UserModel> saveUser(@RequestBody UserModel userModel) {
        ResponseMessage<UserModel> responseMessage = new ResponseMessage<>();
        User user = new UserConverter().convertFromModel(userModel);
        try {
            return responseMessage
                    .prepareSuccessMessage(new UserConverter()
                    .convertFromEntity(userService.create(user)));
        } catch (IllegalArgumentException e) {
            return responseMessage.prepareFailMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return responseMessage.prepareFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @PostMapping("/sign-in")
    public ResponseMessage<String> getAuthHeader(@RequestBody UserAuthorizModel userAuthorizModel) {
        ResponseMessage<String> responseMessage = new ResponseMessage<>();

        try {
            String authHeader = userService.getBasicAuthorizHeaderByAuthorizModel(userAuthorizModel);
            return responseMessage.prepareSuccessMessage(authHeader);
        } catch (IllegalArgumentException e) {
            return responseMessage.prepareFailMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return responseMessage.prepareFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @GetMapping("/admin/get-all")
    public List<UserModel> getAllUser() {
        List<UserModel> userModels = new ArrayList<>();

        for (User user : userService.getAll())
            userModels.add(new UserConverter().convertFromEntity(user));
        return userModels;
    }

    @GetMapping("/get-current")
    public UserModel getCurrentUser() {
        return new UserConverter().convertFromEntity(userService.getCurrentUser());
    }

    @PutMapping("/update")
    public ResponseMessage<UserModel> updateUser(@RequestBody User user) {
        ResponseMessage<UserModel> responseMessage = new ResponseMessage<>();
        try {
            return responseMessage.prepareSuccessMessage(new UserConverter().convertFromEntity(userService.update(user)));
        } catch (IllegalArgumentException e) {
            return responseMessage.prepareFailMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return responseMessage.prepareFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseMessage<UserModel> deleteUser() {
        ResponseMessage<UserModel> responseMessage = new ResponseMessage<>();
        try {
            return responseMessage.prepareSuccessMessage(new UserConverter().convertFromEntity(userService.setInActiveUser()));
        } catch (IllegalArgumentException e) {
            return responseMessage.prepareFailMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return responseMessage.prepareFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}