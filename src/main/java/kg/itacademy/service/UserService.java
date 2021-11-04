package kg.itacademy.service;

import kg.itacademy.entity.User;
import kg.itacademy.model.UserAuthorizModel;

public interface UserService extends BaseService<User> {
    User setInActiveUser();

    User getCurrentUser();

    User getByUsername(String name);

    User getByEmail(String email);

    String getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel);
}