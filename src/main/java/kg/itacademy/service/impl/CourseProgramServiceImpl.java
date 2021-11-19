package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseProgramConverter;
import kg.itacademy.entity.CourseProgram;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.CourseProgramModel;
import kg.itacademy.repository.CourseProgramRepository;
import kg.itacademy.service.CourseProgramService;
import kg.itacademy.util.VariableValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseProgramServiceImpl implements CourseProgramService, VariableValidation<CourseProgram> {

    private final CourseProgramRepository courseProgramRepository;

    @Override
    public CourseProgram save(CourseProgram courseProgram) {
        validateLengthVariables(courseProgram);
        validateVariablesForNullOrIsEmpty(courseProgram);
        return courseProgramRepository.save(courseProgram);
    }

    @Override
    public CourseProgramModel createCourseProgram(CourseProgramModel courseProgramModel) {
        CourseProgram courseProgram = save(new CourseProgramConverter().convertFromModel(courseProgramModel));
        return new CourseProgramConverter().convertFromEntity(courseProgram);
    }

    @Override
    public CourseProgram getById(Long id) {
        return courseProgramRepository.findById(id).orElse(null);
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
        List<CourseProgram> programs = courseProgramRepository.findAllByCourse_Id(id);
        List<CourseProgramModel> programModels = new ArrayList<>();
        for (CourseProgram program : programs)
            programModels.add(new CourseProgramConverter().convertFromEntity(program));
        return programModels;
    }


    public CourseProgram update(CourseProgram courseProgram) {
        validateLengthVariablesForUpdate(courseProgram);
        validateVariablesForNullOrIsEmptyUpdate(courseProgram);
        return courseProgramRepository.save(courseProgram);
    }

    @Override
    public CourseProgramModel updateCurseProgram(CourseProgramModel courseProgramModel) {
        update(new CourseProgramConverter().convertFromModel(courseProgramModel));
        return courseProgramModel;
    }

    @Override
    public CourseProgramModel deleteCourseProgram(Long id) {
        CourseProgram courseProgram = getById(id);
        if (courseProgram == null)
            throw new ApiFailException("Course program by user id (" + id + ") not found");
        courseProgramRepository.delete(courseProgram);
        return new CourseProgramConverter().convertFromEntity(courseProgram);
    }

    @Override
    public void validateLengthVariables(CourseProgram courseProgram) {
        if (courseProgram.getTitle().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for title program");
        if (courseProgram.getDescription().length() > 1000)
            throw new ApiFailException("Exceeded character limit (100) for program description");
    }

    @Override
    public void validateLengthVariablesForUpdate(CourseProgram courseProgram) {
        if (courseProgram.getTitle() != null && courseProgram.getTitle().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for title program");
        if (courseProgram.getDescription() != null && courseProgram.getDescription().length() > 1000)
            throw new ApiFailException("Exceeded character limit (100) for program description");
    }

    @Override
    public void validateVariablesForNullOrIsEmpty(CourseProgram courseProgram) {
        if (courseProgram.getTitle() == null || courseProgram.getTitle().isEmpty())
            throw new ApiFailException("Title program is not filled");
        if (courseProgram.getDescription() == null || courseProgram.getDescription().isEmpty())
            throw new ApiFailException("Program description is not filled");
        if (courseProgram.getCourse().getId() == null)
            throw new ApiFailException("Не указан id курса");
    }

    @Override
    public void validateVariablesForNullOrIsEmptyUpdate(CourseProgram courseProgram) {
        if (courseProgram.getTitle() != null && courseProgram.getTitle().isEmpty())
            throw new ApiFailException("Title program is not filled");
        if (courseProgram.getDescription() != null && courseProgram.getDescription().isEmpty())
            throw new ApiFailException("Program description is not filled");
    }
}