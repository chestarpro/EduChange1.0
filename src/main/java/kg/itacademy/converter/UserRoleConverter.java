package kg.itacademy.converter;

import kg.itacademy.entity.User;
import kg.itacademy.entity.UserRole;
import kg.itacademy.model.UserRoleModel;

public class UserRoleConverter extends BaseConverter<UserRoleModel, UserRole> {

    public UserRoleConverter() {
        super(UserRoleConverter::convertToEntity, UserRoleConverter::convertToModel);
    }

    private static UserRoleModel convertToModel(UserRole entityToConvert) {
        if (entityToConvert == null) return null;

        return UserRoleModel.builder()
                .id(entityToConvert.getId())
                .roleName(entityToConvert.getRoleName())
                .userId(entityToConvert.getUser().getId())
                .build();
    }

    private static UserRole convertToEntity(UserRoleModel modelToConvert) {
        if (modelToConvert == null) return null;

        User user = new User();
        user.setId(modelToConvert.getUserId());

        return UserRole.builder()
                .user(user)
                .roleName(modelToConvert.getRoleName())
                .build();
    }
}
