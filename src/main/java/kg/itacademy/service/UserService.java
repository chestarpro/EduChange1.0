package kg.itacademy.service;

import kg.itacademy.entity.User;
import kg.itacademy.model.user.UserProfileDataModel;
import kg.itacademy.model.user.UserAuthorizModel;
import kg.itacademy.model.user.UserModel;

import java.util.List;

public interface UserService extends BaseService<User> {
    UserProfileDataModel createUser(User user);

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

    UserProfileDataModel getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel);
}