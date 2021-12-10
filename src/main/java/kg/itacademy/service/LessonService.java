package kg.itacademy.service;

import kg.itacademy.entity.Lesson;
import kg.itacademy.model.course.LessonModel;

import java.util.List;

public interface LessonService extends BaseService<Lesson> {
    LessonModel createLesson(LessonModel lessonModel);
    LessonModel getLessonModelById(Long id);
    LessonModel deleteLessonById(Long id);
    List<LessonModel> getAllLessonModel();
    LessonModel updateLesson(LessonModel lessonModel);
    List<LessonModel> getAllByCourseId(Long id);
}
