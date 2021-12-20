package kg.itacademy.model.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLessonModel implements BaseLessonModel{
    private Long id;
    private String lessonInfo;
    private String lessonUrl;

    @Override
    public Long getCourseId() {
        return null;
    }
}