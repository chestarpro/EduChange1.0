package kg.itacademy.converter;

import kg.itacademy.entity.Comment;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserImage;
import kg.itacademy.model.comment.CommentModel;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter extends BaseConverter<CommentModel, Comment> {

    public CommentConverter() {
        super(CommentConverter::convertToEntity, CommentConverter::convertToModel);
    }

    private static CommentModel convertToModel(Comment entityToConvert) {
        if (entityToConvert == null) return null;

        CommentModel commentModel = new CommentModel();
        commentModel.setId(entityToConvert.getId());
        commentModel.setComment(entityToConvert.getCourseComment());
        commentModel.setUserId(entityToConvert.getUser().getId());
        commentModel.setUsername(entityToConvert.getUser().getUsername());
        commentModel.setCourseId(entityToConvert.getCourse().getId());
        if (entityToConvert.getUserImage() != null) {
            commentModel.setUserImageId(entityToConvert.getUserImage().getId());
            commentModel.setUserImageUrl(entityToConvert.getUserImage().getUserImageUrl());
        }

//        return CommentModel.builder()
//                .id(entityToConvert.getId())
//                .comment(entityToConvert.getCourseComment())
//                .userId(entityToConvert.getUser().getId())
//                .username(entityToConvert.getUser().getUsername())
//                .userImageId(entityToConvert.getUserImage().getId())
//                .userImageUrl(entityToConvert.getUserImage().getUserImageUrl())
//                .courseId(entityToConvert.getCourse().getId())
//                .build();

        return commentModel;
    }

    private static Comment convertToEntity(CommentModel modelToConvert) {
        if (modelToConvert == null) return null;

        Comment comment = new Comment();
        comment.setId(modelToConvert.getId());
        comment.setCourseComment(modelToConvert.getComment());

        if (modelToConvert.getUserImageId() != null) {
            UserImage userImage = new UserImage();
            userImage.setId(modelToConvert.getUserImageId());
            comment.setUserImage(userImage);
        }



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