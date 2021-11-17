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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService, VariableValidation<Lesson> {

    private final LessonRepository lessonRepository;

    @Override
    public Lesson save(Lesson lesson) {
        validateVariablesForNullOrIsEmpty(lesson);
        return lessonRepository.save(lesson);
    }

    @Override
    public LessonModel createLesson(LessonModel lessonModel) {
        save(new LessonConverter().convertFromModel(lessonModel));
        return lessonModel;
    }

    @Override
    public Lesson getById(Long id) {
        return lessonRepository.findById(id).orElse(null);
    }

    @Override
    public LessonModel getLessonModelById(Long id) {
        Lesson lesson = getById(id);
        if (lesson == null) {
            throw new ApiFailException("Урок под id: " + id + " не найден");
        }
        return new LessonConverter().convertFromEntity(lesson);
    }

    @Override
    public List<Lesson> getAll() {
        return lessonRepository.findAll();
    }

    @Override
    public List<LessonModel> getAllLessonModel() {
        List<LessonModel> lessonModels = new ArrayList<>();
        for (Lesson lesson : getAll())
            lessonModels.add(new LessonConverter().convertFromEntity(lesson));
        return lessonModels;
    }

    @Override
    public List<LessonModel> getAllByCourseId(Long id) {
        List<Lesson> lessons = lessonRepository.findAllByCourse_Id(id);
        if (lessons == null)
            throw new ApiFailException("Уроки по курсу (id: " + id + ") не найдены");
        List<LessonModel> lessonModels = new ArrayList<>();
        for (Lesson lesson : lessons)
            lessonModels.add(new LessonConverter().convertFromEntity(lesson));
        return lessonModels;
    }

    @Override
    public Lesson update(Lesson lesson) {
        if (lesson.getId() == null)
            throw new IllegalArgumentException("Не указан id урока");
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
        if (lesson == null)
            throw new ApiFailException("Урок под id: " + id + " не найден");
        lessonRepository.delete(lesson);
        return new LessonConverter().convertFromEntity(lesson);
    }

    @Override
    public void validateLengthVariables(Lesson lesson) {

    }

    @Override
    public void validateLengthVariablesForUpdate(Lesson lesson) {

    }

    @Override
    public void validateVariablesForNullOrIsEmpty(Lesson lesson) {
        if (lesson.getLessonInfo() == null || lesson.getLessonInfo().isEmpty())
            throw new IllegalArgumentException("Нет описания урока");
    }

    @Override
    public void validateVariablesForNullOrIsEmptyUpdate(Lesson lesson) {
        if (lesson.getLessonInfo() != null && lesson.getLessonInfo().isEmpty())
            throw new IllegalArgumentException("Нет описания урока");
    }
}