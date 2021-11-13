package kg.itacademy.service.impl;

import kg.itacademy.entity.Comment;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.model.CreateCommentModel;
import kg.itacademy.model.UpdateCommentModel;
import kg.itacademy.repository.CommentRepository;
import kg.itacademy.service.CommentService;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.UserService;
import kg.itacademy.util.VariableValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService, VariableValidation<Comment> {

    private final CommentRepository commentRepository;

    private final CourseService courseService;

    private final UserService userService;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment createCommentByCourseId(CreateCommentModel createCommentModel) {
        if (createCommentModel.getComment().equals("")
                || createCommentModel.getComment().isEmpty()
                || createCommentModel.getComment() == null)
            throw new IllegalArgumentException("Комментарий пустой");

        Course course = courseService.getById(createCommentModel.getCourseId());
        User user = userService.getCurrentUser();
        return save(new Comment(createCommentModel.getComment(), user, course));
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment update(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateByUpdateCommentModel(UpdateCommentModel updateCommentModel) {
        if (updateCommentModel.getId() == null)
            throw new IllegalArgumentException("Не указан id комментария");

        Comment comment = new Comment();
        Course course = courseService.getById(updateCommentModel.getCourseId());
        User user = userService.getCurrentUser();
        comment.setId(updateCommentModel.getId());
        comment.setCourseComment(updateCommentModel.getComment());
        comment.setCourse(course);
        comment.setUser(user);
        return update(comment);
    }

    @Override
    public Comment deleteCommentById(Long id) {
        Comment comment = getById(id);
        commentRepository.delete(comment);
        return comment;
    }

    @Override
    public void validateLengthVariables(Comment comment) {

    }

    @Override
    public void validateVariablesForNullOrIsEmpty(Comment comment) {

    }

    @Override
    public void validateLengthVariablesForUpdateUser(Comment comment) {

    }

    @Override
    public void validateVariablesForNullOrIsEmptyUpdate(Comment comment) {

    }
}