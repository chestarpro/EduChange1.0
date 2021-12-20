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
    private CourseService COURSE_SERVICE;
    @Autowired
    private UserService USER_SERVICE;
    @Autowired
    private UserImageService USER_IMAGE_SERVICE;

    private final CommentRepository COMMENT_REPOSITORY;
    private final CommentConverter COMMENT_CONVERTER;

    @Override
    public Comment save(Comment comment) {
        return COMMENT_REPOSITORY.save(comment);
    }

    @Override
    public CommentModel createComment(CreateCommentModel createCommentModel) {
        validateVariablesForNullOrIsEmpty(createCommentModel);
        validateLengthVariables(createCommentModel);

        Course course = COURSE_SERVICE.getById(createCommentModel.getCourseId());
        User user = USER_SERVICE.getCurrentUser();
        String commentText = createCommentModel.getComment();
        UserImage userImage = USER_IMAGE_SERVICE.getUserImageByUserId(user.getId());
        Comment comment = new Comment(commentText, user, userImage, course);
        save(comment);
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
    public List<CommentModel> getAllCommentModelByCourseId(Long courseId) {
        return COMMENT_REPOSITORY
                .findAllByCourse_Id(courseId)
                .stream()
                .map(COMMENT_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CommentModel updateComment(UpdateCommentModel updateCommentModel) {
        Long commentId = updateCommentModel.getId();
        Comment dataComment = getDataCommentByIdWithCheckAccess(commentId, false);

        validateVariablesForNullOrIsEmpty(updateCommentModel);
        validateLengthVariables(updateCommentModel);

        dataComment.setCourseComment(updateCommentModel.getComment());
        COMMENT_REPOSITORY.save(dataComment);
        return COMMENT_CONVERTER.convertFromEntity(dataComment);
    }

    @Override
    public CommentModel deleteComment(Long id) {
        Comment deleteComment = getDataCommentByIdWithCheckAccess(id, true);
        COMMENT_REPOSITORY.delete(deleteComment);
        return COMMENT_CONVERTER.convertFromEntity(deleteComment);
    }

    private void validateVariablesForNullOrIsEmpty(BaseCommentModel baseCommentModel) {
        if (baseCommentModel.getComment() == null || baseCommentModel.getComment().isEmpty())
            throw new ApiFailException("Comment is not filled");

        if (baseCommentModel instanceof CreateCommentModel) {
            CreateCommentModel createCommentModel = (CreateCommentModel) baseCommentModel;
            if (createCommentModel.getCourseId() == null)
                throw new ApiFailException("Course id is not specified");
            else {
                Long curseId = createCommentModel.getCourseId();
                Course course = COURSE_SERVICE.getById(curseId);
                if (course == null)
                    throw new ApiFailException("Course by id " + curseId + " not found");
            }
        }
    }

    private void validateLengthVariables(BaseCommentModel baseCommentModel) {
        if (baseCommentModel.getComment() != null && baseCommentModel.getComment().length() > 1000)
            throw new ApiFailException("Exceeded character limit (1000) for comment");
    }

    private Comment getDataCommentByIdWithCheckAccess(Long id, boolean isDelete) {
        if (id == null)
            throw new ApiFailException("Comment id is not specified");

        Comment dataComment = getById(id);

        if (dataComment == null)
            throw new ApiFailException("Comment by id " + id + " not found");

        Long currentUserId = USER_SERVICE.getCurrentUser().getId();
        Long authorCommentId = dataComment.getUser().getId();

        if (isDelete) {
            Long authorCourseId = COURSE_SERVICE.getById(dataComment.getCourse().getId()).getUser().getId();
            if (!currentUserId.equals(authorCommentId) && !currentUserId.equals(authorCourseId))
                throw new ApiFailException("Access is denied");
        } else {
            if (!currentUserId.equals(authorCommentId))
                throw new ApiFailException("Access is denied");
        }
        return dataComment;
    }
}