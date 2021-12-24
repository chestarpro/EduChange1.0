package kg.itacademy.service;

import kg.itacademy.entity.User;
import kg.itacademy.model.user.*;
import kg.itacademy.model.userImage.ResetPasswordModel;

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

    UserProfileDataModel getCurrentUserProfileDataModel();

    BaseUserModel getCurrentUserModel();

    List<BaseUserModel> getAllUserModels();

    BaseUserModel resetPassword(ResetPasswordModel resetPasswordModel);

    UserProfileDataModel getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel);
}