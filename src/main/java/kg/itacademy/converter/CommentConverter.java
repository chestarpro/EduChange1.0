package kg.itacademy.converter;

import kg.itacademy.entity.Comment;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.model.CommentModel;

public class CommentConverter extends BaseConverter<CommentModel, Comment> {

    public CommentConverter() {
        super(CommentConverter::convertToEntity, CommentConverter::convertToModel);
    }

    private static CommentModel convertToModel(Comment entityToConvert) {
        if (entityToConvert == null) return null;

        return CommentModel.builder()
                .id(entityToConvert.getId())
                .comment(entityToConvert.getComment())
                .userId(entityToConvert.getUser().getId())
                .courseId(entityToConvert.getCourse().getId())
                .build();
    }

    private static Comment convertToEntity(CommentModel modelToConvert) {
        if (modelToConvert == null) return null;

        User user = new User();
        user.setId(modelToConvert.getUserId());
        Course course = new Course();
        course.setId(modelToConvert.getCourseId());

        return Comment.builder()
                .comment(modelToConvert.getComment())
                .user(user)
                .course(course)
                .build();
    }
}