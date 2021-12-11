package kg.itacademy.service;

import kg.itacademy.entity.Course;
import kg.itacademy.model.course.CourseDataModel;
import kg.itacademy.model.course.CourseModel;

import java.util.List;

public interface CourseService extends BaseService<Course> {
    CourseDataModel createCourse(CourseModel courseModel);

    List<CourseDataModel> getAllByCourseName(String courseName);

    List<CourseDataModel> getAllByCourseCategoryName(String courseName);

    List<CourseDataModel> getAllByCategoryId(Long id);

    List<CourseDataModel> getAllCourseModel();

    CourseModel updateCourse(CourseModel courseModel);

    List<CourseDataModel> getAllByUserId(Long userId);

    CourseDataModel getCourseModelById(Long id);

    CourseModel deleteCourseById(Long id);

    CourseDataModel getCourseDataModelByCourseId(Long courseId);
}