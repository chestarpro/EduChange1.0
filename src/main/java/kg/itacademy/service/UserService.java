package kg.itacademy.service;

import kg.itacademy.entity.User;
import kg.itacademy.model.user.*;

import java.util.List;

public interface UserService extends BaseService<User> {
    UserProfileDataModel createUser(CreateUserModel createUserModel);

    UserProfileDataModel updateUser(UpdateUserModel updateUserModel);

    User setInActiveUser(User user, Long status);

    BaseUserModel deleteUser();

    BaseUserModel deleteUserByAdmin(Long userId);

    User getCurrentUser();

    User getByUsername(String name);

    User getByEmail(String email);

    BaseUserModel getUserModelById(Long id);

    BaseUserModel getCurrentUserModel();

    List<BaseUserModel> getAllUserModels();

    UserProfileDataModel getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel);
}