package kg.itacademy.service;

import kg.itacademy.entity.User;
import kg.itacademy.model.user.*;

import java.util.List;

public interface UserService extends BaseService<User> {
    UserProfileDataModel createUser(CreateUserModel createUserModel);

    User getCurrentUser();

    BaseUser getCurrentUserModel();

    List<BaseUser> getAllUserModels();

    BaseUser getUserModelById(Long id);

    User getByUsername(String name);

    User getByEmail(String email);

    BaseUser updateUser(UpdateUserModel updateUserModel);

    User setInActiveUser(User user, Long status);

    BaseUser deleteUser();

    BaseUser deleteUserByAdmin(Long userId);

    UserProfileDataModel getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel);
}