package kg.itacademy.converter;

import kg.itacademy.entity.User;
import kg.itacademy.entity.UserAuthorizLog;
import kg.itacademy.model.UserAuthorizLogModel;

public class UserAuthorizLogConverter extends BaseConverter<UserAuthorizLogModel, UserAuthorizLog> {

    public UserAuthorizLogConverter() {
        super(UserAuthorizLogConverter::convertToEntity, UserAuthorizLogConverter::convertToModel);
    }

    private static UserAuthorizLogModel convertToModel(UserAuthorizLog entityToConvert) {
        if (entityToConvert == null) return null;

        return UserAuthorizLogModel.builder()
                .id(entityToConvert.getId())
                .userId(entityToConvert.getUser().getId())
                .build();
    }

    private static UserAuthorizLog convertToEntity(UserAuthorizLogModel modelToConvert) {
        if (modelToConvert == null) return null;

        UserAuthorizLog userAuthorizLog = new UserAuthorizLog();
        userAuthorizLog.setId(modelToConvert.getId());
        userAuthorizLog.setCreateDate(modelToConvert.getCreateDate());
        userAuthorizLog.setIsSuccess(modelToConvert.getIsSuccess());

        if (modelToConvert.getUserId() != null) {
            User user = new User();
            user.setId(modelToConvert.getId());
            userAuthorizLog.setUser(user);
        }
        return userAuthorizLog;
    }
}