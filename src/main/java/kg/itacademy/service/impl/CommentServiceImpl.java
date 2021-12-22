package kg.itacademy.service.impl;

import kg.itacademy.converter.CommentConverter;
import kg.itacademy.entity.Comment;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserImage;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.comment.BaseCommentModel;
import kg.itacademy.model.comment.CommentModel;
import kg.itacademy.model.comment.CreateCommentModel;
import kg.itacademy.model.comment.UpdateCommentModel;
import kg.itacademy.repository.CommentRepository;
import kg.itacademy.service.CommentService;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.UserImageService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserImageService userImageService;
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public CommentModel createComment(CreateCommentModel createCommentModel) {
        validateVariablesForNullOrIsEmpty(createCommentModel);
        validateLengthVariables(createCommentModel);

        Course course = courseService.getById(createCommentModel.getCourseId());
        User user = userService.getCurrentUser();
        String commentText = createCommentModel.getComment();

        Comment comment = new Comment();
        comment.setCourseComment(commentText);
        comment.setUser(user);
        comment.setCourse(course);
        UserImage userImage = userImageService.getUserImageByUserId(user.getId());
        if (userImage != null) {
            comment.setUserImageUrl(userImage.getUserImageUrl());
        }
        save(comment);
        return commentConverter.convertFromEntity(comment);
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public CommentModel getCommentModelById(Long id) {
        return commentConverter.convertFromEntity(getById(id));
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<CommentModel> getAllCommentModelByCourseId(Long courseId) {
        return commentRepository
                .findAllByCourse_Id(courseId)
                .stream()
                .map(commentConverter::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CommentModel updateComment(UpdateCommentModel updateCommentModel) {
        Long commentId = updateCommentModel.getId();
        Comment dataComment = getDataCommentByIdWithCheckAccess(commentId, false);

        validateVariablesForNullOrIsEmpty(updateCommentModel);
        validateLengthVariables(updateCommentModel);

        dataComment.setCourseComment(updateCommentModel.getComment());
        commentRepository.save(dataComment);
        return commentConverter.convertFromEntity(dataComment);
    }

    @Override
    public CommentModel deleteComment(Long id) {
        Comment deleteComment = getDataCommentByIdWithCheckAccess(id, true);
        commentRepository.delete(deleteComment);
        return commentConverter.convertFromEntity(deleteComment);
    }

    private void validateVariablesForNullOrIsEmpty(BaseCommentModel baseCommentModel) {
        if (baseCommentModel.getComment() == null || baseCommentModel.getComment().isEmpty())
            throw new ApiFailException("Комментарий не заполнен");

        if (baseCommentModel instanceof CreateCommentModel) {
            CreateCommentModel createCommentModel = (CreateCommentModel) baseCommentModel;
            if (createCommentModel.getCourseId() == null)
                throw new ApiFailException("Не указан ID курса");
            else {
                Long courseId = createCommentModel.getCourseId();
                Course course = courseService.getById(courseId);
                if (course == null)
                    throw new ApiFailException("Курс под ID " + courseId + " не найден");
            }
        }
    }

    private void validateLengthVariables(BaseCommentModel baseCommentModel) {
        if (baseCommentModel.getComment() != null && baseCommentModel.getComment().length() > 1000)
            throw new ApiFailException("Длинна символов ограниченно(1000)");
    }

    private Comment getDataCommentByIdWithCheckAccess(Long id, boolean isDelete) {
        if (id == null)
            throw new ApiFailException("Не указан ID комментария");

        Comment dataComment = getById(id);

        if (dataComment == null)
            throw new ApiFailException("Комментарий под ID " + id + " не найден");

        Long currentUserId = userService.getCurrentUser().getId();
        Long authorCommentId = dataComment.getUser().getId();

        String access = "Доступ ограничен";
        if (isDelete) {
            Long authorCourseId = courseService.getById(dataComment.getCourse().getId()).getUser().getId();
            if (!currentUserId.equals(authorCommentId) && !currentUserId.equals(authorCourseId))
                throw new ApiFailException(access);
        } else {
            if (!currentUserId.equals(authorCommentId))
                throw new ApiFailException(access);
        }
        return dataComment;
    }
}