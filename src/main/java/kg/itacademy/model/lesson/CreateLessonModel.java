package kg.itacademy.model.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLessonModel {
    private String lessonInfo;
    private String lessonUrl;
    private Long courseId;
}