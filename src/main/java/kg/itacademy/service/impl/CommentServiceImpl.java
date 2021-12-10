package kg.itacademy.service.impl;

import kg.itacademy.converter.CommentConverter;
import kg.itacademy.entity.Comment;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.course.CommentModel;
import kg.itacademy.model.course.CreateCommentModel;
import kg.itacademy.model.course.UpdateCommentModel;
import kg.itacademy.repository.CommentRepository;
import kg.itacademy.service.CommentService;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository COMMENT_REPOSITORY;
    private final CourseService COURSE_SERVICE;
    private final UserService USER_SERVICE;
    private final CommentConverter COMMENT_CONVERTER;

    @Override
    public Comment save(Comment comment) {
        return COMMENT_REPOSITORY.save(comment);
    }

    @Override
    public CommentModel createCommentByCourseId(CreateCommentModel createCommentModel) {
        validateLengthVariables(createCommentModel);
        validateVariablesForNullOrIsEmpty(createCommentModel);

        Course course = COURSE_SERVICE.getById(createCommentModel.getCourseId());
        User user = USER_SERVICE.getCurrentUser();

        Comment comment = save(new Comment(createCommentModel.getComment(), user, course));
        return COMMENT_CONVERTER.convertFromEntity(comment);
    }

    @Override
    public Comment getById(Long id) {
        return COMMENT_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public CommentModel getCommentModelById(Long id) {
        return COMMENT_CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public List<Comment> getAll() {
        return COMMENT_REPOSITORY.findAll();
    }

    @Override
    public List<CommentModel> getAllCommentModel() {
        return getAll()
                .stream()
                .map(COMMENT_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentModel> getAllCommentModelByCourseId(Long courseId) {
       return COMMENT_REPOSITORY
               .findAllByCourse_Id(courseId)
               .stream()
               .map(COMMENT_CONVERTER::convertFromEntity)
               .collect(Collectors.toList());
    }

    @Override
    public CommentModel updateByUpdateCommentModel(UpdateCommentModel updateCommentModel) {
        if (updateCommentModel.getId() == null)
            throw new IllegalArgumentException("Не указан id комментария");

        validateVariablesForNullOrIsEmptyUpdate(updateCommentModel);
        validateLengthVariablesForUpdate(updateCommentModel);

        Comment comment = new Comment();
        comment.setId(updateCommentModel.getId());
        comment.setCourseComment(updateCommentModel.getComment());

        return COMMENT_CONVERTER.convertFromEntity(COMMENT_REPOSITORY.save(comment));
    }

    @Override
    public CommentModel deleteComment(Long id) {
        Comment comment = getById(id);
        if (comment == null) {
            throw new ApiFailException("Comment by id " + id + " not found");
        }
        COMMENT_REPOSITORY.delete(comment);
        return COMMENT_CONVERTER.convertFromEntity(comment);
    }

    private void validateLengthVariables(CreateCommentModel comment) {
        if (comment.getComment().length() > 255)
            throw new ApiFailException("Превышен лимит символов 255");
    }

    private void validateVariablesForNullOrIsEmpty(CreateCommentModel comment) {
        if (comment.getComment().isEmpty()
                || comment.getComment() == null)
            throw new ApiFailException("Комментарий пустой");
        if (comment.getCourseId() == null)
            throw new ApiFailException("Не указан id курса");
    }

    private void validateVariablesForNullOrIsEmptyUpdate(UpdateCommentModel comment) {
        if (comment.getComment() == null || comment.getComment().isEmpty())
            throw new ApiFailException("Комментарий пустой");
    }

    private void validateLengthVariablesForUpdate(UpdateCommentModel comment) {
        if (comment.getComment() != null && comment.getComment().length() > 255)
            throw new ApiFailException("Превышен лимит символов 255");
    }
}