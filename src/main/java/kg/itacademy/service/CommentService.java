package kg.itacademy.service;

import kg.itacademy.entity.Comment;
import kg.itacademy.model.CreateCommentModel;
import kg.itacademy.model.UpdateCommentModel;

public interface CommentService extends BaseService<Comment> {
    Comment createCommentByCourseId(CreateCommentModel createCommentModel);
    Comment deleteCommentById(Long id);
    Comment updateByUpdateCommentModel(UpdateCommentModel updateCommentModel);
}
