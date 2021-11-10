package kg.itacademy.converter;

import kg.itacademy.entity.User;
import kg.itacademy.entity.UserImage;
import kg.itacademy.model.UserImageModel;

public class UserImageConverter extends BaseConverter<UserImageModel, UserImage> {

    public UserImageConverter() {
        super(UserImageConverter::convertToEntity, UserImageConverter::convertToModel);

    }

    private static UserImageModel convertToModel(UserImage entityToConvert) {
        if (entityToConvert == null) return null;
        return UserImageModel.builder()
                .id(entityToConvert.getId())
                .userImageUrl(entityToConvert.getUserImageUrl())
                .userId(entityToConvert.getUser().getId())
                .build();
    }

    private static UserImage convertToEntity(UserImageModel modelToConvert) {
        if (modelToConvert == null) return null;

        UserImage userImage = new UserImage();
        userImage.setId(modelToConvert.getId());
        userImage.setUserImageUrl(modelToConvert.getUserImageUrl());

        if (modelToConvert.getUserId() != null) {
            User user = new User();
            user.setId(modelToConvert.getUserId());
            userImage.setUser(user);
        }
        return userImage;
    }
}
