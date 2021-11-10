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

        Like like = new Like();
        like.setId(modelToConvert.getId());

        if (modelToConvert.getUserId() != null) {
            User user = new User();
            user.setId(modelToConvert.getUserId());
            like.setUser(user);
        }

        if (modelToConvert.getCourseId() != null) {
            Course course = new Course();
            course.setId(modelToConvert.getCourseId());
            like.setCourse(course);
        }
        return like;
    }
}