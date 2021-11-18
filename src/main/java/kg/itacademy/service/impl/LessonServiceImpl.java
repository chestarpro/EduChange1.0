package kg.itacademy.service.impl;

import kg.itacademy.converter.LessonConverter;
import kg.itacademy.entity.Lesson;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.LessonModel;
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

    private final LessonRepository lessonRepository;

    @Override
    public Lesson save(Lesson lesson) {
        validateLengthVariables(lesson);
        validateVariablesForNullOrIsEmpty(lesson);
        return lessonRepository.save(lesson);
    }

    @Override
    public LessonModel createLesson(LessonModel lessonModel) {
        Lesson lesson = save(new LessonConverter().convertFromModel(lessonModel));
        return new LessonConverter().convertFromEntity(lesson);
    }

    @Override
    public Lesson getById(Long id) {
        return lessonRepository.findById(id).orElse(null);
    }

    @Override
    public LessonModel getLessonModelById(Long id) {
        Lesson lesson = getById(id);
        return new LessonConverter().convertFromEntity(lesson);
    }

    @Override
    public List<Lesson> getAll() {
        return lessonRepository.findAll();
    }

    @Override
    public List<LessonModel> getAllLessonModel() {
        LessonConverter converter = new LessonConverter();
        return getAll().stream()
                .map(converter::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonModel> getAllByCourseId(Long id) {
        List<Lesson> lessons = lessonRepository.findAllByCourse_Id(id);
        if (!lessons.isEmpty()) {
            LessonConverter converter = new LessonConverter();
            return lessons.stream()
                    .map(converter::convertFromEntity)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Lesson update(Lesson lesson) {
        if (lesson.getId() == null)
            throw new IllegalArgumentException("Lesson id not specified");
        return save(lesson);
    }

    @Override
    public LessonModel updateLesson(LessonModel lessonModel) {
        update(new LessonConverter().convertFromModel(lessonModel));
        return lessonModel;
    }

    @Override
    public LessonModel deleteLessonById(Long id) {
        Lesson lesson = getById(id);
        lessonRepository.delete(lesson);
        return new LessonConverter().convertFromEntity(lesson);
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
            throw new IllegalArgumentException("The description of the lesson is not specified");
    }

    @Override
    public void validateVariablesForNullOrIsEmptyUpdate(Lesson lesson) {
        if (lesson.getLessonInfo() != null && lesson.getLessonInfo().isEmpty())
            throw new IllegalArgumentException("The description of the lesson is not specified");
    }
}