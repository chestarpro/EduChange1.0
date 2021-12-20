package kg.itacademy.converter;

import kg.itacademy.entity.Comment;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.model.comment.CommentModel;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter extends BaseConverter<CommentModel, Comment> {

    public CommentConverter() {
        super(CommentConverter::convertToEntity, CommentConverter::convertToModel);
    }

    private static CommentModel convertToModel(Comment entityToConvert) {
        if (entityToConvert == null) return null;

        return CommentModel.builder()
                .id(entityToConvert.getId())
                .comment(entityToConvert.getCourseComment())
                .userId(entityToConvert.getUser().getId())
                .username(entityToConvert.getUser().getUsername())
                .userImageUrl(entityToConvert.getUserImage())
                .courseId(entityToConvert.getCourse().getId())
                .build();
    }

    private static Comment convertToEntity(CommentModel modelToConvert) {
        if (modelToConvert == null) return null;

        Comment comment = new Comment();
        comment.setId(modelToConvert.getId());
        comment.setCourseComment(modelToConvert.getComment());
        comment.setUserImage(modelToConvert.getUserImageUrl());

        if (modelToConvert.getCourseId() != null) {
            Course course = new Course();
            course.setId(modelToConvert.getCourseId());
            comment.setCourse(course);
        }

        if (modelToConvert.getUserId() != null) {
            User user = new User();
            user.setId(modelToConvert.getUserId());
            comment.setUser(user);
        }
        return comment;
    }
}