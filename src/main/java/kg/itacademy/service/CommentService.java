package kg.itacademy.service;

import kg.itacademy.entity.Comment;

public interface CommentService extends BaseService<Comment> {
    Comment deleteComment();
}
