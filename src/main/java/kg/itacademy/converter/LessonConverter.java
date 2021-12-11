package kg.itacademy.converter;

import kg.itacademy.entity.Course;
import kg.itacademy.entity.Lesson;
import kg.itacademy.model.lesson.LessonModel;
import org.springframework.stereotype.Component;

@Component
public class LessonConverter extends BaseConverter<LessonModel, Lesson> {

    public LessonConverter() {
        super(LessonConverter::convertToEntity, LessonConverter::convertToModel);
    }

    private static LessonModel convertToModel(Lesson entityToConvert) {
        if (entityToConvert == null) return null;

        return LessonModel.builder()
                .id(entityToConvert.getId())
                .lessonInfo(entityToConvert.getLessonInfo())
                .lessonUrl(entityToConvert.getLessonUrl())
                .courseId(entityToConvert.getCourse().getId())
                .build();
    }

    private static Lesson convertToEntity(LessonModel modelToConvert) {
        if (modelToConvert == null) return null;

        Lesson lesson = new Lesson();
        lesson.setId(modelToConvert.getId());
        lesson.setLessonInfo(modelToConvert.getLessonInfo());
        lesson.setLessonUrl(modelToConvert.getLessonUrl());

        if (modelToConvert.getCourseId() != null) {
            Course course = new Course();
            course.setId(modelToConvert.getCourseId());
            lesson.setCourse(course);
        }
        return lesson;
    }
}