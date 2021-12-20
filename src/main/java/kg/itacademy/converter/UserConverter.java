package kg.itacademy.converter;

import kg.itacademy.entity.User;
import kg.itacademy.model.user.*;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends BaseConverter<BaseUserModel, User> {

    public UserConverter() {
        super(UserConverter::convertToEntity, UserConverter::convertToModel);
    }

    private static BaseUserModel convertToModel(User entityToConvert) {
        if (entityToConvert == null) return null;

        return UserModelToSend.builder()
                .id(entityToConvert.getId())
                .fullName(entityToConvert.getFullName())
                .birthDay(entityToConvert.getBirthDay())
                .username(entityToConvert.getUsername())
                .email(entityToConvert.getEmail())
                .isActive(entityToConvert.getIsActive())
                .build();
    }

    private static User convertToEntity(BaseUserModel modelToConvert) {
        if (modelToConvert == null) return null;

        User user = new User();

        if (modelToConvert instanceof UpdateUserModel) {
            user.setId(((UpdateUserModel) modelToConvert).getId());
        }
        user.setFullName(modelToConvert.getFullName());
        user.setUsername(modelToConvert.getUsername());
        user.setEmail(modelToConvert.getEmail());
        user.setPassword(modelToConvert.getPassword());

        return user;
    }
}