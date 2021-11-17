package kg.itacademy.service;

import kg.itacademy.entity.User;
import kg.itacademy.model.UserAuthorizModel;
import kg.itacademy.model.UserModel;

import java.util.List;

public interface UserService extends BaseService<User> {
    UserModel createUser(User user);

    List<UserModel> getAllUserModels();

    User setInActiveUser(User user, Long status);

    UserModel getUserModelById(Long id);

    UserModel deleteUser();

    User getCurrentUser();

    UserModel getCurrentUserModel();

    UserModel updateUser(User user);

    User getByUsername(String name);

    User getByEmail(String email);

    String getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel);
}