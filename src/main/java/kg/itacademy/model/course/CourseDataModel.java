package kg.itacademy.model.course;

import kg.itacademy.model.comment.CommentModel;
import kg.itacademy.model.CourseImageModel;
import kg.itacademy.model.courseProgram.CourseProgramModel;
import kg.itacademy.model.LikeModel;
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
    private String authorFullName;
    private CourseModel courseModel;
    private CourseImageModel imageModel;
    private Long lessonCount;
    private List<CourseProgramModel> programs;
    private List<LikeModel> likes;
    private List<CommentModel> comments;
}