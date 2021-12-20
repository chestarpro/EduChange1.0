package kg.itacademy.service;

import kg.itacademy.entity.Comment;
import kg.itacademy.model.comment.CommentModel;
import kg.itacademy.model.comment.CreateCommentModel;
import kg.itacademy.model.comment.UpdateCommentModel;

import java.util.List;

public interface CommentService extends BaseService<Comment> {
    CommentModel createComment(CreateCommentModel createCommentModel);

    CommentModel updateComment(UpdateCommentModel updateCommentModel);

    CommentModel deleteComment(Long id);

    CommentModel getCommentModelById(Long id);

    List<CommentModel> getAllCommentModelByCourseId(Long id);
}