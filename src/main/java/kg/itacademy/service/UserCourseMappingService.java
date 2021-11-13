package kg.itacademy.service;



import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.model.CourseModel;
import kg.itacademy.model.UserCourseMappingModel;

import java.util.List;
public interface UserCourseMappingService extends BaseService<UserCourseMapping> {
    List<CourseModel> findAllPurchasedCourses();
    UserCourseMappingModel createByIdCourse(Long id);
}
