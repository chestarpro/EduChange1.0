package kg.itacademy.model.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommentModel {
    private Long id;
    private String comment;
    private Long userId;
    private String username;
    private Long courseId;
}