package kg.itacademy.service.impl;

import kg.itacademy.converter.LessonConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.Lesson;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.lesson.BaseLessonModel;
import kg.itacademy.model.lesson.CreateLessonModel;
import kg.itacademy.model.lesson.LessonModel;
import kg.itacademy.model.lesson.UpdateLessonModel;
import kg.itacademy.repository.LessonRepository;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.LessonService;
import kg.itacademy.service.UserCourseMappingService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserCourseMappingService userCourseMappingService;
    private final LessonRepository lessonRepository;
    private final LessonConverter lessonConverter;

    @Override
    public Lesson save(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public LessonModel createLesson(CreateLessonModel createLessonModel) {
        validateVariablesForNullOrIsEmpty(createLessonModel);
        validateLengthVariables(createLessonModel);

        Lesson lesson = new Lesson();
        lesson.setLessonInfo(createLessonModel.getLessonInfo());
        lesson.setLessonUrl(createLessonModel.getLessonUrl());
        lesson.setIsVisible(createLessonModel.getIsVisible());

        Course course = new Course();
        course.setId(createLessonModel.getCourseId());
        lesson.setCourse(course);
        save(lesson);
        return lessonConverter.convertFromEntity(lesson);
    }

    @Override
    public Lesson getById(Long id) {
        return lessonRepository.findById(id).orElse(null);
    }

    @Override
    public LessonModel getLessonModelById(Long id) {
        return lessonConverter.convertFromEntity(getById(id));
    }

    @Override
    public List<Lesson> getAll() {
        return lessonRepository.findAll();
    }

    @Override
    public Long getCountLessonByCourseId(Long courseId) {
        return lessonRepository.getCountLessonByCourseId(courseId);
    }

    @Override
    public List<LessonModel> getAllByCourseId(Long courseId) {
        return lessonRepository
                .findAllByCourse_IdOrderByIdAsc(courseId)
                .stream()
                .map(lessonConverter::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonModel> getFirstThreeLessonsByCourseId(Long courseId) {
        return lessonRepository
                .findFirstThreeLessonsByCourseId(courseId)
                .stream()
                .map(lessonConverter::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public LessonModel updateLesson(UpdateLessonModel updateLessonModel) {
        Long lessonId = updateLessonModel.getId();
        Lesson dataLesson = getDataLessonByIdWithCheckAccess(lessonId);

        validateVariablesForNullOrIsEmptyUpdate(updateLessonModel);
        validateLengthVariables(updateLessonModel);

        setVariablesForUpdateLesson(dataLesson, updateLessonModel);

        lessonRepository.save(dataLesson);
        return lessonConverter.convertFromEntity(dataLesson);
    }

    @Override
    public LessonModel deleteLessonById(Long id) {
        Lesson deleteLesson = getDataLessonByIdWithCheckAccess(id);
        lessonRepository.delete(deleteLesson);
        return lessonConverter.convertFromEntity(deleteLesson);
    }

    private Lesson getDataLessonByIdWithCheckAccess(Long id) {
        if (id == null)
            throw new ApiFailException("???? ???????????? ID ??????????");

        Lesson dataLesson = getById(id);

        if (dataLesson == null)
            throw new ApiFailException("???????? ?????? ID " + id + " ???? ????????????");

        Long currentUserId = userService.getCurrentUser().getId();
        Long authorCourseId = dataLesson.getCourse().getUser().getId();

        if (!currentUserId.equals(authorCourseId))
            throw new ApiFailException("???????????? ??????????????????");

        return dataLesson;
    }
    
    private void validateLengthVariables(BaseLessonModel baseLessonModel) {
        if (baseLessonModel.getLessonInfo() != null && baseLessonModel.getLessonInfo().length() > 1000)
            throw new ApiFailException("???????????? ???????????????? ???????????????????? ?????????? ??????????????????????(1000)");
    }

    private void validateVariablesForNullOrIsEmpty(CreateLessonModel createLessonModel) {
        if (createLessonModel.getLessonInfo() == null || createLessonModel.getLessonInfo().isEmpty())
            throw new ApiFailException("???????????????????? ???? ?????????? ???? ??????????????????");
        if (createLessonModel.getIsVisible() == null)
            throw new ApiFailException("Lesson visible is not specified");
        if (createLessonModel.getCourseId() == null)
            throw new ApiFailException("???? ?????????? ID ??????????");
        else {
            Long courseId = createLessonModel.getCourseId();
            Course course = courseService.getById(courseId);
            if (course == null)
                throw new ApiFailException("???????? ?????? ID " + courseId + " ???? ????????????");
        }
    }

    private void validateVariablesForNullOrIsEmptyUpdate(UpdateLessonModel updateLessonModel) {
        if (updateLessonModel.getLessonInfo() != null && updateLessonModel.getLessonInfo().isEmpty())
            throw new ApiFailException("???????????????????? ???? ?????????? ???? ??????????????????");
    }

    private void setVariablesForUpdateLesson(Lesson lesson, UpdateLessonModel updateLessonModel) {
        if (updateLessonModel.getLessonUrl() != null)
            lesson.setLessonUrl(updateLessonModel.getLessonUrl());
        if (updateLessonModel.getLessonInfo() != null)
            lesson.setLessonInfo(updateLessonModel.getLessonInfo());
        if (updateLessonModel.getIsVisible() != null)
            lesson.setIsVisible(updateLessonModel.getIsVisible());
    }
}