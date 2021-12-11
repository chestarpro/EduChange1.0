package kg.itacademy.service;

import kg.itacademy.entity.Lesson;
import kg.itacademy.model.lesson.CreateLessonModel;
import kg.itacademy.model.lesson.LessonModel;
import kg.itacademy.model.lesson.UpdateLessonModel;

import java.util.List;

public interface LessonService extends BaseService<Lesson> {
    LessonModel createLesson(CreateLessonModel createLessonModel);
    LessonModel getLessonModelById(Long id);
    LessonModel deleteLessonById(Long id);
    List<LessonModel> getAllLessonModel();
    LessonModel updateLesson(UpdateLessonModel updateLessonModel);
    List<LessonModel> getAllByCourseId(Long id);
}
