package kg.itacademy.service;


import kg.itacademy.entity.Course;
import kg.itacademy.entity.UserCourseMapping;

import java.util.List;
public interface UserCourseMappingService extends BaseService<UserCourseMapping> {
    List<Course> findAllMyPurchasedCourses();
    UserCourseMapping createByIdCourse(Long id);
}
