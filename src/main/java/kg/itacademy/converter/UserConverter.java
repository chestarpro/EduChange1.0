package kg.itacademy.converter;

import kg.itacademy.entity.User;
import kg.itacademy.model.UserModel;

public class UserConverter extends BaseConverter<UserModel, User> {

    public UserConverter() {
        super(UserConverter::convertToEntity, UserConverter::convertToModel);
    }

    private static UserModel convertToModel(User entityToConvert) {
        if (entityToConvert == null) return null;

        return UserModel.builder()
                .id(entityToConvert.getId())
                .fullName(entityToConvert.getFullName())
                .username(entityToConvert.getUsername())
                .email(entityToConvert.getEmail())
                .isActive(entityToConvert.getIsActive())
                .build();
    }

    private static User convertToEntity(UserModel modelToConvert) {

        throw new UnsupportedOperationException("Conversation from ClientModel to Client is not supported");
    }
}