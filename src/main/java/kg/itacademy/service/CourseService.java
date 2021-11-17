package kg.itacademy.service;

import kg.itacademy.entity.Course;
import kg.itacademy.model.CourseModel;
import kg.itacademy.model.UserModel;

import java.util.List;

public interface CourseService extends BaseService<Course> {
    CourseModel createCourse(CourseModel courseModel);

    List<CourseModel> getAllByCourseName(String courseName);

    List<CourseModel> getAllByCourseCategoryName(String courseName);

    List<CourseModel> getAllByCategoryId(Long id);

    List<CourseModel> getAllCourseModel();

    CourseModel updateCourse(CourseModel courseModel);

    List<Course> findAllCreatedCoursesByUserId();

    public CourseModel getCourseModelById(Long id);

    CourseModel deleteCourseById(Long id);
}