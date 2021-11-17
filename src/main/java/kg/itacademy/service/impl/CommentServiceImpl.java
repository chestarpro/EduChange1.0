package kg.itacademy.service.impl;

import kg.itacademy.converter.CommentConverter;
import kg.itacademy.entity.Comment;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.CommentModel;
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

import java.util.ArrayList;
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
        validateLengthVariables(comment);
        validateVariablesForNullOrIsEmpty(comment);
        return commentRepository.save(comment);
    }

    @Override
    public CommentModel createCommentByCourseId(CreateCommentModel createCommentModel) {
        Course course = courseService.getById(createCommentModel.getCourseId());
        User user = userService.getCurrentUser();
        Comment comment = save(new Comment(createCommentModel.getComment(), user, course));
        return new CommentConverter().convertFromEntity(comment);
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public CommentModel getCommentModelById(Long id) {
        Comment comment = getById(id);
        if (comment == null)
            throw new ApiFailException("Комментарий не найден");
        return new CommentConverter().convertFromEntity(comment);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<CommentModel> getAllCommentModel() {
        List<CommentModel> list = new ArrayList<>();
        for (Comment comment : getAll())
            list.add(new CommentConverter().convertFromEntity(comment));
        return list;
    }

    @Override
    public List<CommentModel> getAllCommentModelByCourseId(Long id) {
        List<Comment> comments = commentRepository.findAllByCourse_Id(id);
        List<CommentModel> commentModels = new ArrayList<>();
        for (Comment comment : comments)
            commentModels.add(new CommentConverter().convertFromEntity(comment));
        return commentModels;
    }

    @Override
    public Comment update(Comment comment) {
        if (comment.getId() == null)
            throw new IllegalArgumentException("Не указан id комментария");

        validateLengthVariablesForUpdate(comment);
        validateVariablesForNullOrIsEmptyUpdate(comment);
        return commentRepository.save(comment);
    }

    @Override
    public CommentModel updateByUpdateCommentModel(UpdateCommentModel updateCommentModel) {
        Comment comment = new Comment();
        Course course = courseService.getById(updateCommentModel.getCourseId());
        User user = userService.getCurrentUser();
        comment.setId(updateCommentModel.getId());
        comment.setCourseComment(updateCommentModel.getComment());
        comment.setCourse(course);
        comment.setUser(user);
        return new CommentConverter().convertFromEntity(update(comment));
    }

    @Override
    public CommentModel deleteCommentById(Long id) {
        Comment comment = getById(id);
        if (comment == null) {
            throw new ApiFailException("Комментарий под id " + id + " не найден");
        }
        commentRepository.delete(comment);
        return new CommentConverter().convertFromEntity(comment);
    }

    @Override
    public void validateLengthVariables(Comment comment) {
        if (comment.getCourseComment().length() > 255)
            throw new ApiFailException("Превышен лимит символов 255");
    }

    @Override
    public void validateVariablesForNullOrIsEmptyUpdate(Comment comment) {
        if (comment.getCourseComment() != null && comment.getCourseComment().length() > 255)
            throw new ApiFailException("Превышен лимит символов 255");
    }

    @Override
    public void validateVariablesForNullOrIsEmpty(Comment comment) {
        if (comment.getCourseComment().isEmpty()
                || comment.getCourseComment() == null)
            throw new ApiFailException("Комментарий пустой");
        if (comment.getCourse() == null)
            throw new ApiFailException("Не указан id курса");
        if (comment.getUser() == null)
            throw new ApiFailException("Не указан id пользователя");
    }

    @Override
    public void validateLengthVariablesForUpdate(Comment comment) {
        if (comment.getCourseComment() != null && comment.getCourseComment().isEmpty())
            throw new ApiFailException("Комментарий пустой");
    }
}