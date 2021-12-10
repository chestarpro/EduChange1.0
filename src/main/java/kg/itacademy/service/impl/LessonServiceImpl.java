package kg.itacademy.service.impl;

import kg.itacademy.converter.LessonConverter;
import kg.itacademy.entity.Lesson;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.course.LessonModel;
import kg.itacademy.repository.LessonRepository;
import kg.itacademy.service.LessonService;
import kg.itacademy.util.VariableValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService, VariableValidation<Lesson> {

    private final LessonRepository LESSON_REPOSITORY;
    private final LessonConverter LESSON_CONVERTER;

    @Override
    public Lesson save(Lesson lesson) {
        validateLengthVariables(lesson);
        validateVariablesForNullOrIsEmpty(lesson);
        return LESSON_REPOSITORY.save(lesson);
    }

    @Override
    public LessonModel createLesson(LessonModel lessonModel) {
        Lesson lesson = save(LESSON_CONVERTER.convertFromModel(lessonModel));
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

    public Lesson update(Lesson lesson) {
        if (lesson.getId() == null)
            throw new ApiFailException("Lesson id not specified");
        validateVariablesForNullOrIsEmptyUpdate(lesson);
        validateLengthVariablesForUpdate(lesson);
        return LESSON_REPOSITORY.save(lesson);
    }

    @Override
    public LessonModel updateLesson(LessonModel lessonModel) {
        update(LESSON_CONVERTER.convertFromModel(lessonModel));
        return lessonModel;
    }

    @Override
    public LessonModel deleteLessonById(Long id) {
        Lesson lesson = getById(id);

        if (lesson == null)
            throw new ApiFailException("Lesson by id " + id + " not found");
        LESSON_REPOSITORY.delete(lesson);
        return LESSON_CONVERTER.convertFromEntity(lesson);
    }

    @Override
    public void validateLengthVariables(Lesson lesson) {
        if (lesson.getLessonInfo().length() > 1000)
            throw new ApiFailException("Exceeded character limit (1000) for lesson info");
    }

    @Override
    public void validateLengthVariablesForUpdate(Lesson lesson) {
        if (lesson.getLessonInfo() != null && lesson.getLessonInfo().length() > 1000)
            throw new ApiFailException("Exceeded character limit (1000) for lesson info");
    }

    @Override
    public void validateVariablesForNullOrIsEmpty(Lesson lesson) {
        if (lesson.getLessonInfo() == null || lesson.getLessonInfo().isEmpty())
            throw new ApiFailException("The description of the lesson is not specified");
    }

    @Override
    public void validateVariablesForNullOrIsEmptyUpdate(Lesson lesson) {
        if (lesson.getLessonInfo() != null && lesson.getLessonInfo().isEmpty())
            throw new ApiFailException("The description of the lesson is not specified");
    }
}