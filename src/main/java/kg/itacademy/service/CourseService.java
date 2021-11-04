package kg.itacademy.service;

import kg.itacademy.entity.Course;

import java.util.List;

public interface CourseService extends BaseService<Course> {
    List<Course> findAllByCourseName(String courseName);
    List<Course> findAllByCourseCategoryName(String courseName);
    List<Course> findAllByCategoryId(Long id);
    List<Course> findAllMyCourse();
    Course deleteCourseById(Long id);
}