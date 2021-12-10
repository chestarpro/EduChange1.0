package kg.itacademy.model.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CourseProgramModel {
    private Long id;
    private String title;
    private String description;
    private Long courseId;
}
