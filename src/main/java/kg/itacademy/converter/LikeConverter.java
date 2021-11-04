package kg.itacademy.converter;

import kg.itacademy.entity.Course;
import kg.itacademy.entity.Like;
import kg.itacademy.entity.User;
import kg.itacademy.model.LikeModel;

public class LikeConverter extends BaseConverter<LikeModel, Like> {
    public LikeConverter() {
        super(LikeConverter::convertToEntity, LikeConverter::convertToModel);
    }

    private static LikeModel convertToModel(Like entityToConvert) {
        if (entityToConvert == null) return null;

        return LikeModel.builder()
                .id(entityToConvert.getId())
                .userId(entityToConvert.getUser().getId())
                .courseId(entityToConvert.getCourse().getId())
                .build();
    }

    private static Like convertToEntity(LikeModel modelToConvert) {
        if (modelToConvert == null) return null;

        User user = new User();
        user.setId(modelToConvert.getUserId());
        Course course = new Course();
        course.setId(modelToConvert.getCourseId());

        return Like.builder()
                .user(user)
                .course(course)
                .build();
    }
}