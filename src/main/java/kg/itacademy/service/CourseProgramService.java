package kg.itacademy.service;

import kg.itacademy.entity.CourseProgram;
import kg.itacademy.model.CourseProgramModel;

import java.util.List;

public interface CourseProgramService extends BaseService<CourseProgram> {
    CourseProgramModel createCourseProgram(CourseProgramModel courseProgramModel);
    CourseProgramModel getCourseProgramModelById(Long id);
    List<CourseProgramModel> getAllCourseProgramModelByCourseId(Long courseId);
    CourseProgramModel updateCurseProgram(CourseProgramModel courseProgramModel);
    CourseProgramModel deleteCourseProgram(Long id);
}
