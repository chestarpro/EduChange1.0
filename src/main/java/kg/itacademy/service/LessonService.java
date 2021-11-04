package kg.itacademy.service;

import kg.itacademy.entity.Lesson;

public interface LessonService extends BaseService<Lesson> {
    Lesson deleteLesson();
}
