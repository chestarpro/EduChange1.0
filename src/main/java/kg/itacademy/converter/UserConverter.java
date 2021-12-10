package kg.itacademy.converter;

import kg.itacademy.entity.User;
import kg.itacademy.exception.ApiErrorException;
import kg.itacademy.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends BaseConverter<UserModel, User> {

    public UserConverter() {
        super(UserConverter::convertToEntity, UserConverter::convertToModel);
    }

    private static UserModel convertToModel(User entityToConvert) {
        if (entityToConvert == null) return null;

        return UserModel.builder()
                .id(entityToConvert.getId())
                .fullName(entityToConvert.getFullName())
                .birthDay(entityToConvert.getBirthDay())
                .username(entityToConvert.getUsername())
                .email(entityToConvert.getEmail())
                .isActive(entityToConvert.getIsActive())
                .build();
    }

    private static User convertToEntity(UserModel modelToConvert) {
        throw new ApiErrorException("Conversation from ClientModel to Client is not supported");
    }
}