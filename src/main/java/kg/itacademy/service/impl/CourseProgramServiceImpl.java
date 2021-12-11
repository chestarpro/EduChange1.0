package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseProgramConverter;
import kg.itacademy.entity.CourseProgram;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.courseProgram.CourseProgramModel;
import kg.itacademy.repository.CourseProgramRepository;
import kg.itacademy.service.CourseProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseProgramServiceImpl implements CourseProgramService {

    private final CourseProgramRepository COURSE_PROGRAM_REPOSITORY;
    private final CourseProgramConverter COURSE_PROGRAM_CONVERTER;

    @Override
    public CourseProgram save(CourseProgram courseProgram) {

        return COURSE_PROGRAM_REPOSITORY.save(courseProgram);
    }

    @Override
    public CourseProgramModel createCourseProgram(CourseProgramModel courseProgramModel) {
        validateLengthVariables(courseProgramModel);
        validateVariablesForNullOrIsEmpty(courseProgramModel);
        CourseProgram courseProgram = save(COURSE_PROGRAM_CONVERTER.convertFromModel(courseProgramModel));
        return COURSE_PROGRAM_CONVERTER.convertFromEntity(courseProgram);
    }

    @Override
    public CourseProgram getById(Long id) {
        return COURSE_PROGRAM_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public CourseProgramModel getCourseProgramModelById(Long id) {
        return new CourseProgramConverter().convertFromEntity(getById(id));
    }

    @Override
    public List<CourseProgram> getAll() {
        return null;
    }

    @Override
    public List<CourseProgramModel> getAllCourseProgramModelByCourseId(Long id) {
        return COURSE_PROGRAM_REPOSITORY.findAllByCourse_Id(id)
                .stream().map(COURSE_PROGRAM_CONVERTER::convertFromEntity).collect(Collectors.toList());
    }

    @Override
    public CourseProgramModel updateCurseProgram(CourseProgramModel courseProgramModel) {
        validateLengthVariablesForUpdate(courseProgramModel);
        validateVariablesForNullOrIsEmptyUpdate(courseProgramModel);
        COURSE_PROGRAM_REPOSITORY.save(COURSE_PROGRAM_CONVERTER.convertFromModel(courseProgramModel));
        return courseProgramModel;
    }

    @Override
    public CourseProgramModel deleteCourseProgram(Long id) {
        CourseProgram courseProgram = getById(id);
        if (courseProgram == null)
            throw new ApiFailException("Course program by user id " + id + ") not found");
        COURSE_PROGRAM_REPOSITORY.delete(courseProgram);
        return new CourseProgramConverter().convertFromEntity(courseProgram);
    }


    public void validateLengthVariables(CourseProgramModel courseProgram) {
        if (courseProgram.getTitle().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for title program");
        if (courseProgram.getDescription().length() > 1000)
            throw new ApiFailException("Exceeded character limit (100) for program description");
    }


    public void validateLengthVariablesForUpdate(CourseProgramModel courseProgram) {
        if (courseProgram.getTitle() != null && courseProgram.getTitle().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for title program");
        if (courseProgram.getDescription() != null && courseProgram.getDescription().length() > 1000)
            throw new ApiFailException("Exceeded character limit (100) for program description");
    }


    public void validateVariablesForNullOrIsEmpty(CourseProgramModel courseProgram) {
        if (courseProgram.getTitle() == null || courseProgram.getTitle().isEmpty())
            throw new ApiFailException("Title program is not filled");
        if (courseProgram.getDescription() == null || courseProgram.getDescription().isEmpty())
            throw new ApiFailException("Program description is not filled");
        if (courseProgram.getCourseId() == null)
            throw new ApiFailException("Не указан id курса");
    }


    public void validateVariablesForNullOrIsEmptyUpdate(CourseProgramModel courseProgram) {
        if (courseProgram.getTitle() != null && courseProgram.getTitle().isEmpty())
            throw new ApiFailException("Title program is not filled");
        if (courseProgram.getDescription() != null && courseProgram.getDescription().isEmpty())
            throw new ApiFailException("Program description is not filled");
    }
}