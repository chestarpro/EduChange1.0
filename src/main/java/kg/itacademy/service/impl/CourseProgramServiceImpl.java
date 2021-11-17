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

    @Override
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
            throw new ApiFailException("Программа курса с id: " + id + " не найдена");
        courseProgramRepository.delete(courseProgram);
        return new CourseProgramConverter().convertFromEntity(courseProgram);
    }

    @Override
    public void validateLengthVariables(CourseProgram courseProgram) {
        if (courseProgram.getTitle().length() > 50)
            throw new ApiFailException("Заголовок программы превысил 50 символов");
        if (courseProgram.getDescription().length() > 1000)
            throw new ApiFailException("Описание программы превысило 1000 символов");
    }

    @Override
    public void validateLengthVariablesForUpdate(CourseProgram courseProgram) {
        if (courseProgram.getTitle() != null && courseProgram.getTitle().length() > 50)
            throw new ApiFailException("Заголовок программы превысило 50 символов");
        if (courseProgram.getDescription() != null && courseProgram.getDescription().length() > 1000)
            throw new ApiFailException("Описание программы превысило 1000 символов");
    }

    @Override
    public void validateVariablesForNullOrIsEmpty(CourseProgram courseProgram) {
        if (courseProgram.getTitle() == null || courseProgram.getTitle().isEmpty())
            throw new ApiFailException("Не указан заголовок программы");
        if (courseProgram.getDescription() == null || courseProgram.getDescription().isEmpty())
            throw new ApiFailException("Нет описания программы");
        if (courseProgram.getCourse().getId() == null)
            throw new ApiFailException("Не указан id курса");
    }

    @Override
    public void validateVariablesForNullOrIsEmptyUpdate(CourseProgram courseProgram) {
        if (courseProgram.getTitle() != null && courseProgram.getTitle().isEmpty())
            throw new ApiFailException("Не указан заголовок программы");
        if (courseProgram.getDescription() != null && courseProgram.getDescription().isEmpty())
            throw new ApiFailException("Нет описания программы");
    }
}