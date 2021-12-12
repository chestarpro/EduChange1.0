package kg.itacademy.service;

import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.model.course.CourseDataModel;
import kg.itacademy.model.UserCourseMappingModel;

import java.util.List;

public interface UserCourseMappingService extends BaseService<UserCourseMapping> {
    UserCourseMappingModel createByCourseId(Long courseId);

    UserCourseMappingModel deleteMapping(Long id);

    UserCourseMappingModel getUserCourseMappingModelById(Long id);

    List<CourseDataModel> getAllPurchasedCourses(Long userId);
}