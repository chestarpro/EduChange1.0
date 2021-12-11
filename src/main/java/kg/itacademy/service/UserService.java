package kg.itacademy.service;

import kg.itacademy.entity.User;
import kg.itacademy.model.user.*;

import java.util.List;

public interface UserService extends BaseService<User> {
    UserProfileDataModel createUser(CreateUserModel createUserModel);

    User getCurrentUser();

    BaseUserModel getCurrentUserModel();

    List<BaseUserModel> getAllUserModels();

    BaseUserModel getUserModelById(Long id);

    User getByUsername(String name);

    User getByEmail(String email);

    BaseUserModel updateUser(UpdateUserModel updateUserModel);

    User setInActiveUser(User user, Long status);

    BaseUserModel deleteUser();

    BaseUserModel deleteUserByAdmin(Long userId);

    UserProfileDataModel getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel);
}