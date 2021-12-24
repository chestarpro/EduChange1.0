package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseProgramConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.CourseProgram;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.courseProgram.BaseCourseProgramModel;
import kg.itacademy.model.courseProgram.CourseProgramModel;
import kg.itacademy.model.courseProgram.CreateCourseProgramModel;
import kg.itacademy.model.courseProgram.UpdateCourseProgramModel;
import kg.itacademy.repository.CourseProgramRepository;
import kg.itacademy.service.CourseProgramService;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseProgramServiceImpl implements CourseProgramService {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;
    private final CourseProgramRepository courseProgramRepository;
    private final CourseProgramConverter courseProgramConverter;

    @Override
    public CourseProgram save(CourseProgram courseProgram) {
        return courseProgramRepository.save(courseProgram);
    }

    @Override
    public CourseProgramModel createCourseProgram(CreateCourseProgramModel createCourseProgramModel) {
        validateVariablesForNullOrIsEmpty(createCourseProgramModel);
        validateLengthVariables(createCourseProgramModel);

        CourseProgram courseProgram = new CourseProgram();
        courseProgram.setTitle(createCourseProgramModel.getTitle());
        courseProgram.setDescription(createCourseProgramModel.getDescription());
        Course course = new Course();
        course.setId(createCourseProgramModel.getCourseId());
        courseProgram.setCourse(course);
        save(courseProgram);
        return courseProgramConverter.convertFromEntity(courseProgram);
    }

    @Override
    public CourseProgram getById(Long id) {
        return courseProgramRepository.findById(id).orElse(null);
    }

    @Override
    public CourseProgramModel getCourseProgramModelById(Long id) {
        return courseProgramConverter.convertFromEntity(getById(id));
    }

    @Override
    public List<CourseProgram> getAll() {
        return courseProgramRepository.findAll();
    }

    @Override
    public List<CourseProgramModel> getAllCourseProgramModelByCourseId(Long id) {
        return courseProgramRepository.findAllByCourse_Id(id)
                .stream()
                .map(courseProgramConverter::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CourseProgramModel updateCurseProgram(UpdateCourseProgramModel updateCourseProgramModel) {
        Long programId = updateCourseProgramModel.getId();
        CourseProgram dataCourseProgram = getDataCourseProgramByIdWithCheckAccess(programId);

        validateLengthVariables(updateCourseProgramModel);
        validateVariablesForNullOrIsEmptyUpdate(updateCourseProgramModel);

        setVariablesForUpdateCourseProgram(dataCourseProgram, updateCourseProgramModel);
        courseProgramRepository.save(dataCourseProgram);
        return courseProgramConverter.convertFromEntity(dataCourseProgram);
    }

    @Override
    public CourseProgramModel deleteCourseProgram(Long id) {
        CourseProgram deleteProgram = getDataCourseProgramByIdWithCheckAccess(id);
        courseProgramRepository.delete(deleteProgram);
        return courseProgramConverter.convertFromEntity(deleteProgram);
    }

    private CourseProgram getDataCourseProgramByIdWithCheckAccess(Long id) {
        if (id == null)
            throw new ApiFailException("Не указан ID программы курса");

        CourseProgram dataCourseProgram = getById(id);

        if (dataCourseProgram == null)
            throw new ApiFailException("Программа курса под ID " + id + " не найдена");

        Long currentUserId = userService.getCurrentUser().getId();
        Long authorCourseId = dataCourseProgram.getCourse().getUser().getId();

        if (!currentUserId.equals(authorCourseId))
            throw new ApiFailException("Доступ ограничен");

        return dataCourseProgram;
    }

    private void validateLengthVariables(BaseCourseProgramModel baseCourseProgramModel) {
        if (baseCourseProgramModel.getTitle() != null && baseCourseProgramModel.getTitle().length() > 50)
            throw new ApiFailException("Длинна символов заголовка программы ограниченно(50)");
        if (baseCourseProgramModel.getDescription() != null && baseCourseProgramModel.getDescription().length() > 1000)
            throw new ApiFailException("Длинна символов описании программы ограниченно(1000)");
    }

    private void validateVariablesForNullOrIsEmpty(CreateCourseProgramModel createCourseProgramModel) {
        if (createCourseProgramModel.getTitle() == null || createCourseProgramModel.getTitle().isEmpty())
            throw new ApiFailException("Заголовок программы не заполнен");
        if (createCourseProgramModel.getDescription() == null || createCourseProgramModel.getDescription().isEmpty())
            throw new ApiFailException("Описание программы не заполнено");
        if (createCourseProgramModel.getCourseId() == null)
            throw new ApiFailException("Не указан ID курса");
        else {
            Long courseId = createCourseProgramModel.getCourseId();
            Course course = courseService.getById(courseId);
            if (course == null)
                throw new ApiFailException("Курс под ID " + courseId + " не найден");
        }
    }

    private void validateVariablesForNullOrIsEmptyUpdate(UpdateCourseProgramModel updateCourseProgramModel) {
        if (updateCourseProgramModel.getTitle() != null && updateCourseProgramModel.getTitle().isEmpty())
            throw new ApiFailException("Заголовок программы не заполнен");
        if (updateCourseProgramModel.getDescription() != null && updateCourseProgramModel.getDescription().isEmpty())
            throw new ApiFailException("Описание программы не заполнено");
    }

    private void setVariablesForUpdateCourseProgram(CourseProgram courseProgram, UpdateCourseProgramModel updateCourseProgramModel) {
        if (updateCourseProgramModel.getTitle() != null)
            courseProgram.setTitle(updateCourseProgramModel.getTitle());

        if (updateCourseProgramModel.getDescription() != null)
            courseProgram.setDescription(updateCourseProgramModel.getDescription());
    }
}