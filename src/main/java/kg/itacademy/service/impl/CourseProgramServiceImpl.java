package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseProgramConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.CourseProgram;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.courseProgram.CourseProgramModel;
import kg.itacademy.model.courseProgram.CreateCourseProgramModel;
import kg.itacademy.model.courseProgram.UpdateCourseProgramModel;
import kg.itacademy.repository.CourseProgramRepository;
import kg.itacademy.service.CourseProgramService;
import kg.itacademy.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseProgramServiceImpl implements CourseProgramService {

    private final CourseProgramRepository COURSE_PROGRAM_REPOSITORY;
    private final CourseProgramConverter COURSE_PROGRAM_CONVERTER;
    @Autowired
    private CourseService COURSE_SERVICE;

    @Override
    public CourseProgram save(CourseProgram courseProgram) {
        return COURSE_PROGRAM_REPOSITORY.save(courseProgram);
    }

    @Override
    public CourseProgramModel createCourseProgram(CreateCourseProgramModel createCourseProgramModel) {
        validateLengthVariables(createCourseProgramModel);
        validateVariablesForNullOrIsEmpty(createCourseProgramModel);
        CourseProgram courseProgram = new CourseProgram();
        courseProgram.setTitle(createCourseProgramModel.getTitle());
        courseProgram.setDescription(createCourseProgramModel.getDescription());
        Course course = new Course();
        course.setId(createCourseProgramModel.getCourseId());
        courseProgram.setCourse(course);
        return COURSE_PROGRAM_CONVERTER.convertFromEntity(courseProgram);
    }

    @Override
    public CourseProgram getById(Long id) {
        return COURSE_PROGRAM_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public CourseProgramModel getCourseProgramModelById(Long id) {
        return COURSE_PROGRAM_CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public List<CourseProgram> getAll() {
        return null;
    }

    @Override
    public List<CourseProgramModel> getAllCourseProgramModelByCourseId(Long id) {
        return COURSE_PROGRAM_REPOSITORY.findAllByCourse_Id(id)
                .stream()
                .map(COURSE_PROGRAM_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CourseProgramModel updateCurseProgram(UpdateCourseProgramModel updateCourseProgramModel) {
        Long programId = updateCourseProgramModel.getId();

        if (programId == null)
            throw new ApiFailException("Не указан id программы");

        CourseProgram dataCourseProgram = getById(programId);

        if (dataCourseProgram == null)
            throw new ApiFailException("Course program by id " + programId + " not found");

        validateLengthVariablesForUpdate(updateCourseProgramModel);
        validateVariablesForNullOrIsEmptyUpdate(updateCourseProgramModel);

        setForUpdateProgram(dataCourseProgram, updateCourseProgramModel);

        COURSE_PROGRAM_REPOSITORY.save(dataCourseProgram);

        return COURSE_PROGRAM_CONVERTER.convertFromEntity(dataCourseProgram);
    }

    @Override
    public CourseProgramModel deleteCourseProgram(Long id) {
        CourseProgram courseProgram = getById(id);
        if (courseProgram == null)
            throw new ApiFailException("Course program by user id " + id + ") not found");
        COURSE_PROGRAM_REPOSITORY.delete(courseProgram);
        return new CourseProgramConverter().convertFromEntity(courseProgram);
    }


    public void validateLengthVariables(CreateCourseProgramModel createCourseProgramModel) {
        if (createCourseProgramModel.getTitle().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for title program");
        if (createCourseProgramModel.getDescription().length() > 1000)
            throw new ApiFailException("Exceeded character limit (100) for program description");
    }


    public void validateLengthVariablesForUpdate(UpdateCourseProgramModel updateCourseProgramModel) {
        if (updateCourseProgramModel.getTitle() != null && updateCourseProgramModel.getTitle().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for title program");
        if (updateCourseProgramModel.getDescription() != null && updateCourseProgramModel.getDescription().length() > 1000)
            throw new ApiFailException("Exceeded character limit (100) for program description");
    }


    public void validateVariablesForNullOrIsEmpty(CreateCourseProgramModel createCourseProgramModel) {
        if (createCourseProgramModel.getTitle() == null || createCourseProgramModel.getTitle().isEmpty())
            throw new ApiFailException("Title program is not filled");
        if (createCourseProgramModel.getDescription() == null || createCourseProgramModel.getDescription().isEmpty())
            throw new ApiFailException("Program description is not filled");
        if (createCourseProgramModel.getCourseId() == null)
            throw new ApiFailException("Не указан id курса");
        else {
            Long curseId = createCourseProgramModel.getCourseId();
            Course course = COURSE_SERVICE.getById(curseId);
            if (course == null)
                throw new ApiFailException("Курс не найден по id " + curseId);
        }
    }


    public void validateVariablesForNullOrIsEmptyUpdate(UpdateCourseProgramModel updateCourseProgramModel) {
        if (updateCourseProgramModel.getTitle() != null && updateCourseProgramModel.getTitle().isEmpty())
            throw new ApiFailException("Title program is not filled");
        if (updateCourseProgramModel.getDescription() != null && updateCourseProgramModel.getDescription().isEmpty())
            throw new ApiFailException("Program description is not filled");
    }

    private void setForUpdateProgram(CourseProgram courseProgram, UpdateCourseProgramModel updateCourseProgramModel) {
        if (updateCourseProgramModel.getTitle() != null)
            courseProgram.setTitle(updateCourseProgramModel.getTitle());

        if (updateCourseProgramModel.getDescription() != null)
            courseProgram.setDescription(updateCourseProgramModel.getDescription());
    }
}