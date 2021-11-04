package kg.itacademy.converter;

import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.model.UserCourseMappingModel;

public class UserCourseMappingConverter extends BaseConverter<UserCourseMappingModel, UserCourseMapping> {

    public UserCourseMappingConverter() {
        super(UserCourseMappingConverter::convertToEntity, UserCourseMappingConverter::convertToModel);
    }

    private static UserCourseMappingModel convertToModel(UserCourseMapping entityToConvert) {
        if (entityToConvert == null) return null;

        return UserCourseMappingModel.builder()
                .id(entityToConvert.getId())
                .userId(entityToConvert.getUser().getId())
                .courseId(entityToConvert.getCourse().getId())
                .build();
    }

    private static UserCourseMapping convertToEntity(UserCourseMappingModel modelToConvert) {
        if (modelToConvert == null) return null;

        User user = new User();
        Course course = new Course();
        user.setId(modelToConvert.getUserId());
        course.setId(modelToConvert.getCourseId());

        return UserCourseMapping.builder()
                .user(user)
                .course(course)
                .build();
    }
}
