package kg.itacademy.model.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDataModel {
    private CourseModel courseModel;
    private CourseImageModel imageModel;
    private List<CourseProgramModel> programs;
    private List<LikeModel> likes;
    private List<CommentModel> comments;
}