package kg.itacademy.service;

import kg.itacademy.entity.Course;
import kg.itacademy.model.course.CourseDataModel;
import kg.itacademy.model.course.CourseModel;
import kg.itacademy.model.course.CreateCourseModel;
import kg.itacademy.model.course.UpdateCourseModel;

import java.util.List;

public interface CourseService extends BaseService<Course> {
    CourseDataModel createCourse(CreateCourseModel createCourseModel);

    CourseDataModel updateCourse(UpdateCourseModel updateCourseModel);

    CourseModel deleteCourseById(Long id);

    CourseDataModel getCourseModelById(Long id);

    CourseDataModel getCourseDataModelByCourseId(Long courseId);

    List<CourseDataModel> getAllCourseDataModel();

    List<CourseDataModel> getAllCourseDataModelByUserId(Long userId);

    List<CourseDataModel> getAllCourseDataModelByCategoryId(Long id);

    List<CourseDataModel> getAllCourseDataModelByCourseName(String courseName);

    List<CourseDataModel> getAllCourseDataModelByCategoryName(String courseName);
}