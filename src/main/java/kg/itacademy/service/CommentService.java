package kg.itacademy.service;

import kg.itacademy.entity.Comment;
import kg.itacademy.model.comment.CommentModel;
import kg.itacademy.model.comment.CreateCommentModel;
import kg.itacademy.model.comment.UpdateCommentModel;

import java.util.List;

public interface CommentService extends BaseService<Comment> {
    CommentModel createCommentByCourseId(CreateCommentModel createCommentModel);
    CommentModel getCommentModelById(Long id);
    CommentModel deleteComment(Long id);
    List<CommentModel> getAllCommentModel();
    List<CommentModel> getAllCommentModelByCourseId(Long id);
    CommentModel updateByUpdateCommentModel(UpdateCommentModel updateCommentModel);
}
