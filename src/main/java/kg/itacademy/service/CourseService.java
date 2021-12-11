package kg.itacademy.service;

import kg.itacademy.entity.Course;
import kg.itacademy.model.course.CourseDataModel;
import kg.itacademy.model.course.CourseModel;
import kg.itacademy.model.course.CreateCourseModel;
import kg.itacademy.model.course.UpdateCourseModel;

import java.util.List;

public interface CourseService extends BaseService<Course> {
    CourseDataModel createCourse(CreateCourseModel createCourseModel);

    List<CourseDataModel> getAllByCourseName(String courseName);

    List<CourseDataModel> getAllByCourseCategoryName(String courseName);

    List<CourseDataModel> getAllByCategoryId(Long id);

    List<CourseDataModel> getAllCourseModel();

    CourseModel updateCourse(UpdateCourseModel updateCourseModel);

    List<CourseDataModel> getAllByUserId(Long userId);

    CourseDataModel getCourseModelById(Long id);

    CourseModel deleteCourseById(Long id);

    CourseDataModel getCourseDataModelByCourseId(Long courseId);
}