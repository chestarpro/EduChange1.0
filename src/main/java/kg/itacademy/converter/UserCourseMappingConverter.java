package kg.itacademy.converter;

import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.model.UserCourseMappingModel;
import org.springframework.stereotype.Component;

@Component
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

        UserCourseMapping userCourseMapping = new UserCourseMapping();
        userCourseMapping.setId(modelToConvert.getId());

        if (modelToConvert.getUserId() != null) {
            User user = new User();
            user.setId(modelToConvert.getUserId());
            userCourseMapping.setUser(user);
        }
        if (modelToConvert.getCourseId() != null) {
            Course course = new Course();
            course.setId(modelToConvert.getCourseId());
            userCourseMapping.setCourse(course);
        }

        return userCourseMapping;
    }
}
