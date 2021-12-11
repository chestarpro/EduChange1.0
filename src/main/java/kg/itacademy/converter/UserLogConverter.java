package kg.itacademy.converter;

import kg.itacademy.entity.User;
import kg.itacademy.entity.UserLog;
import kg.itacademy.model.UserLogModel;
import org.springframework.stereotype.Component;

@Component
public class UserLogConverter extends BaseConverter<UserLogModel, UserLog> {

    public UserLogConverter() {
        super(UserLogConverter::convertToEntity, UserLogConverter::convertToModel);
    }

    private static UserLogModel convertToModel(UserLog entityToConvert) {
        if (entityToConvert == null) return null;

        return UserLogModel.builder()
                .id(entityToConvert.getId())
                .userId(entityToConvert.getUser().getId())
                .createDate(entityToConvert.getCreateDate())
                .isSuccess(entityToConvert.getIsSuccess())
                .build();
    }

    private static UserLog convertToEntity(UserLogModel modelToConvert) {
        if (modelToConvert == null) return null;

        UserLog userLog = new UserLog();
        userLog.setId(modelToConvert.getId());
        userLog.setCreateDate(modelToConvert.getCreateDate());
        userLog.setIsSuccess(modelToConvert.getIsSuccess());

        if (modelToConvert.getUserId() != null) {
            User user = new User();
            user.setId(modelToConvert.getId());
            userLog.setUser(user);
        }
        return userLog;
    }
}