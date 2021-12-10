package kg.itacademy.service;

import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.model.CourseModel;
import kg.itacademy.model.UserCourseMappingModel;

import java.util.List;

public interface UserCourseMappingService extends BaseService<UserCourseMapping> {

    UserCourseMappingModel getUserCourseMappingModelById(Long id);

    List<UserCourseMappingModel> getAllUserCourseMappingModel();

    List<CourseModel> getAllPurchasedCourses(Long userId);

    UserCourseMappingModel createByCourseId(Long courseId);

    UserCourseMappingModel deleteMapping(Long id);
}
