package kg.itacademy.service;

import kg.itacademy.entity.Lesson;
import kg.itacademy.model.lesson.CreateLessonModel;
import kg.itacademy.model.lesson.LessonModel;
import kg.itacademy.model.lesson.UpdateLessonModel;

import java.util.List;

public interface LessonService extends BaseService<Lesson> {
    LessonModel createLesson(CreateLessonModel createLessonModel);

    LessonModel updateLesson(UpdateLessonModel updateLessonModel);

    LessonModel deleteLessonById(Long id);

    LessonModel getLessonModelById(Long id);

    List<LessonModel> getAllByCourseId(Long id);
}