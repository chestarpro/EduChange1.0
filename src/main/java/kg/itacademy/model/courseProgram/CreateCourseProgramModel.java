package kg.itacademy.model.courseProgram;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseProgramModel {
    private String title;
    private String description;
    private Long courseId;
}