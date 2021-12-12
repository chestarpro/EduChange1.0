package kg.itacademy.service;

import kg.itacademy.entity.CourseProgram;
import kg.itacademy.model.courseProgram.CourseProgramModel;
import kg.itacademy.model.courseProgram.CreateCourseProgramModel;
import kg.itacademy.model.courseProgram.UpdateCourseProgramModel;

import java.util.List;

public interface CourseProgramService extends BaseService<CourseProgram> {
    CourseProgramModel createCourseProgram(CreateCourseProgramModel createCourseProgramModel);

    CourseProgramModel updateCurseProgram(UpdateCourseProgramModel updateCourseProgramModel);

    CourseProgramModel deleteCourseProgram(Long id);

    CourseProgramModel getCourseProgramModelById(Long id);

    List<CourseProgramModel> getAllCourseProgramModelByCourseId(Long courseId);
}