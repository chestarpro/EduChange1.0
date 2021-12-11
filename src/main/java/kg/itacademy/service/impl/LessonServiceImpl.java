package kg.itacademy.service.impl;

import kg.itacademy.converter.LessonConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.Lesson;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.lesson.CreateLessonModel;
import kg.itacademy.model.lesson.LessonModel;
import kg.itacademy.model.lesson.UpdateLessonModel;
import kg.itacademy.repository.LessonRepository;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository LESSON_REPOSITORY;
    private final LessonConverter LESSON_CONVERTER;
    private final CourseService COURSE_SERVICE;

    @Override
    public Lesson save(Lesson lesson) {
        return LESSON_REPOSITORY.save(lesson);
    }

    @Override
    public LessonModel createLesson(CreateLessonModel createLessonModel) {

        validateVariablesForNullOrIsEmpty(createLessonModel);
        validateLengthVariables(createLessonModel);

        Lesson lesson = new Lesson();
        lesson.setLessonInfo(createLessonModel.getLessonInfo());
        lesson.setLessonUrl(createLessonModel.getLessonUrl());

        Course course = new Course();
        course.setId(createLessonModel.getCourseId());
        lesson.setCourse(course);

        return LESSON_CONVERTER.convertFromEntity(lesson);
    }

    @Override
    public Lesson getById(Long id) {
        return LESSON_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public LessonModel getLessonModelById(Long id) {
        Lesson lesson = getById(id);
        return new LessonConverter().convertFromEntity(lesson);
    }

    @Override
    public List<Lesson> getAll() {
        return LESSON_REPOSITORY.findAll();
    }

    @Override
    public List<LessonModel> getAllLessonModel() {
        return getAll().stream()
                .map(LESSON_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonModel> getAllByCourseId(Long id) {
        return LESSON_REPOSITORY.findAllByCourse_Id(id).stream()
                .map(LESSON_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public LessonModel updateLesson(UpdateLessonModel updateLessonModel) {
        Long lessonId = updateLessonModel.getId();
        if (lessonId == null)
            throw new ApiFailException("Lesson id not specified");

        Lesson dataLesson = getById(lessonId);

        if (dataLesson == null)
            throw new ApiFailException("Lesson by id " + lessonId + " not found");

        validateVariablesForNullOrIsEmptyUpdate(updateLessonModel);
        validateLengthVariablesForUpdate(updateLessonModel);

        setForUpdateLesson(dataLesson, updateLessonModel);

        LESSON_REPOSITORY.save(dataLesson);
        return LESSON_CONVERTER.convertFromEntity(dataLesson);
    }

    @Override
    public LessonModel deleteLessonById(Long id) {
        Lesson lesson = getById(id);

        if (lesson == null)
            throw new ApiFailException("Lesson by id " + id + " not found");
        LESSON_REPOSITORY.delete(lesson);
        return LESSON_CONVERTER.convertFromEntity(lesson);
    }


    public void validateLengthVariables(CreateLessonModel createLessonModel) {
        if (createLessonModel.getLessonInfo().length() > 1000)
            throw new ApiFailException("Exceeded character limit (1000) for lesson info");
    }

    public void validateLengthVariablesForUpdate(UpdateLessonModel updateLessonModel) {
        if (updateLessonModel.getLessonInfo() != null && updateLessonModel.getLessonInfo().length() > 1000)
            throw new ApiFailException("Exceeded character limit (1000) for lesson info");
    }

    public void validateVariablesForNullOrIsEmpty(CreateLessonModel createLessonModel) {
        if (createLessonModel.getLessonInfo() == null || createLessonModel.getLessonInfo().isEmpty())
            throw new ApiFailException("The description of the lesson is not specified");
        if (createLessonModel.getCourseId() == null)
            throw new ApiFailException("Не указан id курса");
        else {
            Long curseId = createLessonModel.getCourseId();
            Course course = COURSE_SERVICE.getById(curseId);
            if (course == null)
                throw new ApiFailException("Курс не найден по id " + curseId);
        }
    }

    public void validateVariablesForNullOrIsEmptyUpdate(UpdateLessonModel updateLessonModel) {
        if (updateLessonModel.getLessonInfo() != null && updateLessonModel.getLessonInfo().isEmpty())
            throw new ApiFailException("The description of the lesson is not specified");
    }

    private void setForUpdateLesson(Lesson lesson, UpdateLessonModel updateLessonModel) {
        if (updateLessonModel.getLessonUrl() != null)
            lesson.setLessonUrl(updateLessonModel.getLessonUrl());
        if (updateLessonModel.getLessonInfo() != null)
            lesson.setLessonInfo(updateLessonModel.getLessonInfo());
    }
}