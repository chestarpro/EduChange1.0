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

        if (modelToConvert instanceof CreateUserModel) {
            user.setFullName(((CreateUserModel) modelToConvert).getFullName());
            user.setUsername(((CreateUserModel) modelToConvert).getUsername());
            user.setEmail(((CreateUserModel) modelToConvert).getEmail());
            user.setIsActive(((CreateUserModel) modelToConvert).getIsActive());
            user.setPassword(((CreateUserModel) modelToConvert).getPassword());
        } else if (modelToConvert instanceof UpdateUserModel) {
            user.setId(((UpdateUserModel) modelToConvert).getId());
            user.setFullName(((UpdateUserModel) modelToConvert).getFullName());
            user.setUsername(((UpdateUserModel) modelToConvert).getUsername());
            user.setEmail(((UpdateUserModel) modelToConvert).getEmail());
            user.setIsActive(((UpdateUserModel) modelToConvert).getIsActive());
            user.setPassword(((UpdateUserModel) modelToConvert).getPassword());
        }
        return user;
    }
}