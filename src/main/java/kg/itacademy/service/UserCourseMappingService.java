package kg.itacademy.service;

import kg.itacademy.entity.User;
import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.model.course.CourseDataModel;
import kg.itacademy.model.UserCourseMappingModel;
import kg.itacademy.model.user.BaseUserModel;

import java.util.List;

public interface UserCourseMappingService extends BaseService<UserCourseMapping> {
    UserCourseMappingModel createByCourseId(Long courseId);

    UserCourseMappingModel getUserCourseMappingModelById(Long id);

    List<BaseUserModel> getAllCustomersByCourseId(Long courseId);

    List<CourseDataModel> getAllPurchasedCourses(Long userId);

    UserCourseMapping getByCourseIdAndUserId(Long courseId, Long userId);
}