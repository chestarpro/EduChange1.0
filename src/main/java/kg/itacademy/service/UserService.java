package kg.itacademy.service;

import kg.itacademy.entity.User;
import kg.itacademy.model.UserAuthorizModel;
import kg.itacademy.model.UserModel;

import java.util.List;

public interface UserService extends BaseService<User> {
    UserModel createUser(User user);

    User getCurrentUser();

    UserModel getCurrentUserModel();

    List<UserModel> getAllUserModels();

    UserModel getUserModelById(Long id);

    User getByUsername(String name);

    User getByEmail(String email);

    void validateEmail(String email);

    UserModel updateUser(User user);

    User setInActiveUser(User user, Long status);

    UserModel deleteUser();

    UserModel deleteUserByAdmin(Long userId);

    String getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel);
}