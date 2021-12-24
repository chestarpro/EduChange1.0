package kg.itacademy.model.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLessonModel implements BaseLessonModel {
    private String lessonInfo;
    private String lessonUrl;
    private Boolean isVisible;
    private Long courseId;

    @Override
    public Long getId() {
        return null;
    }
}