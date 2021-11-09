package kg.itacademy.converter;

import kg.itacademy.entity.Category;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.model.CourseModel;

public class CourseConverter extends BaseConverter<CourseModel, Course> {

    public CourseConverter() {
        super(CourseConverter::convertToEntity, CourseConverter::convertToModel);
    }

    private static CourseModel convertToModel(Course entityToConvert) {
        if (entityToConvert == null) return null;

        return CourseModel.builder()
                .id(entityToConvert.getId())
                .categoryId(entityToConvert.getCategory().getId())
                .courseName(entityToConvert.getCourseName())
                .email(entityToConvert.getEmail())
                .phoneNumber(entityToConvert.getPhoneNumber())
                .courseInfo(entityToConvert.getCourseInfo())
                .courseInfoUrl(entityToConvert.getCourseInfoUrl())
                .price(entityToConvert.getPrice())
                .userId(entityToConvert.getUser().getId())
                .build();
    }

    private static Course convertToEntity(CourseModel modelToConvert) {
        if (modelToConvert == null) return null;

        Course course = new Course();
        course.setId(modelToConvert.getId());
        course.setCourseName(modelToConvert.getCourseName());
        course.setEmail(modelToConvert.getEmail());
        course.setPhoneNumber(modelToConvert.getPhoneNumber());
        course.setCourseInfo(modelToConvert.getCourseInfo());
        course.setCourseInfoUrl(modelToConvert.getCourseInfoUrl());
        course.setPrice(modelToConvert.getPrice());

        Category category = new Category();
        category.setId(modelToConvert.getCategoryId());

        User user = new User();
        user.setId(modelToConvert.getUserId());

        if (modelToConvert.getCategoryId() != null)
            course.setCategory(category);
        if (modelToConvert.getUserId() != null)
            course.setUser(user);

        return course;
    }
}