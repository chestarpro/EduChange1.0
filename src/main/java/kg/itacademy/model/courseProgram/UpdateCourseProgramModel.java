package kg.itacademy.model.courseProgram;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseProgramModel implements BaseCourseProgramModel {
    private Long id;
    private String title;
    private String description;

    @Override
    public Long getCourseId() {
        return null;
    }
}