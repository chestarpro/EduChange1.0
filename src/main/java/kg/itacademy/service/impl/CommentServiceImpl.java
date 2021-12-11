package kg.itacademy.service.impl;

import kg.itacademy.converter.CommentConverter;
import kg.itacademy.entity.Comment;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.comment.CommentModel;
import kg.itacademy.model.comment.CreateCommentModel;
import kg.itacademy.model.comment.UpdateCommentModel;
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
public class CommentServiceImpl implements CommentService {

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
        Long commentId = updateCommentModel.getId();

        if (commentId == null)
            throw new ApiFailException("Не указан id комментария");

        Comment dataComment = getById(commentId);

        if (dataComment == null)
            throw new ApiFailException("Comment by id " + commentId + " not found");

        if (!dataComment.getUser().getId().equals(USER_SERVICE.getCurrentUser().getId()))
            throw new ApiFailException("Доступ запрещен!");

        validateVariablesForNullOrIsEmptyUpdate(updateCommentModel);
        validateLengthVariablesForUpdate(updateCommentModel);

        dataComment.setCourseComment(updateCommentModel.getComment());
        COMMENT_REPOSITORY.save(dataComment);

        return COMMENT_CONVERTER.convertFromEntity(dataComment);
    }

    @Override
    public CommentModel deleteComment(Long id) {
        Comment comment = getById(id);

        if (comment == null) {
            throw new ApiFailException("Comment by id " + id + " not found");
        }
        Long currentUserId = USER_SERVICE.getCurrentUser().getId();
        Long authorCourseId = COURSE_SERVICE.getById(comment.getCourse().getId()).getUser().getId();

        if (!currentUserId.equals(comment.getUser().getId()) && !currentUserId.equals(authorCourseId)) {
            throw new ApiFailException("Access is denied");
        }

        COMMENT_REPOSITORY.delete(comment);
        return COMMENT_CONVERTER.convertFromEntity(comment);
    }

    private void validateLengthVariables(CreateCommentModel comment) {
        if (comment.getComment().length() > 255)
            throw new ApiFailException("Exceeded character limit (255) for comment");
    }

    private void validateVariablesForNullOrIsEmpty(CreateCommentModel createCommentModel) {
        if (createCommentModel.getComment().isEmpty() || createCommentModel.getComment() == null)
            throw new ApiFailException("Comment is not filled");
        if (createCommentModel.getCourseId() == null)
            throw new ApiFailException("Course id is not filled");
        else {
            Long curseId = createCommentModel.getCourseId();
            Course course = COURSE_SERVICE.getById(curseId);
            if (course == null)
                throw new ApiFailException("Курс не найден по id " + curseId);
        }
    }

    private void validateVariablesForNullOrIsEmptyUpdate(UpdateCommentModel comment) {
        if (comment.getComment() == null || comment.getComment().isEmpty())
            throw new ApiFailException("Comment is not filled");
    }

    private void validateLengthVariablesForUpdate(UpdateCommentModel comment) {
        if (comment.getComment() != null && comment.getComment().length() > 255)
            throw new ApiFailException("Exceeded character limit (255) for comment");
    }
}