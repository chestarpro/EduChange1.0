package kg.itacademy.converter;

import kg.itacademy.entity.Course;
import kg.itacademy.entity.CourseProgram;
import kg.itacademy.model.course.CourseProgramModel;
import org.springframework.stereotype.Component;

@Component
public class CourseProgramConverter extends BaseConverter<CourseProgramModel, CourseProgram> {

    public CourseProgramConverter() {
        super(CourseProgramConverter::convertToEntity, CourseProgramConverter::convertToModel);
    }

    private static CourseProgramModel convertToModel(CourseProgram entityToConvert) {
        if (entityToConvert == null) return null;

        return CourseProgramModel.builder()
                .id(entityToConvert.getId())
                .title(entityToConvert.getTitle())
                .description(entityToConvert.getDescription())
                .courseId(entityToConvert.getCourse().getId())
                .build();
    }

    private static CourseProgram convertToEntity(CourseProgramModel modelToConvert) {
        if (modelToConvert == null) return null;

        CourseProgram courseProgram = new CourseProgram();
        courseProgram.setId(modelToConvert.getId());
        courseProgram.setTitle(modelToConvert.getTitle());
        courseProgram.setDescription(modelToConvert.getDescription());

        if (modelToConvert.getCourseId() != null) {
            Course course = new Course();
            course.setId(modelToConvert.getCourseId());
            courseProgram.setCourse(course);
        }
        return courseProgram;
    }
}