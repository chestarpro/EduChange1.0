package kg.itacademy.service;

import kg.itacademy.entity.UserRole;
import kg.itacademy.model.user.UserRoleModel;

import java.util.List;

public interface UserRoleService extends BaseService<UserRole> {
    List<UserRoleModel> getAllUserRoleModel();
}
