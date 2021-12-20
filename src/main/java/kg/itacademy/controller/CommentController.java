package kg.itacademy.controller;

import kg.itacademy.model.comment.CommentModel;
import kg.itacademy.model.comment.CreateCommentModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.model.comment.UpdateCommentModel;
import kg.itacademy.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService COMMENT_SERVICE;

    @PostMapping("/create")
    public ResponseMessage<CommentModel> save(@RequestBody CreateCommentModel createCommentModel) {
        return new ResponseMessage<CommentModel>()
                .prepareSuccessMessage(COMMENT_SERVICE.createComment(createCommentModel));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<CommentModel> getById(@PathVariable Long id) {
        return new ResponseMessage<CommentModel>()
                .prepareSuccessMessage(COMMENT_SERVICE.getCommentModelById(id));
    }

    @GetMapping("/get-all/by-course-id/{courseId}")
    public ResponseMessage<List<CommentModel>> getAllByCourseId(@PathVariable Long courseId) {
        return new ResponseMessage<List<CommentModel>>()
                .prepareSuccessMessage(COMMENT_SERVICE.getAllCommentModelByCourseId(courseId));
    }

    @PutMapping("/update")
    public ResponseMessage<CommentModel> update(@RequestBody UpdateCommentModel updateCommentModel) {
        return new ResponseMessage<CommentModel>()
                .prepareSuccessMessage(COMMENT_SERVICE.updateComment(updateCommentModel));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseMessage<CommentModel> delete(@PathVariable Long id) {
        return new ResponseMessage<CommentModel>()
                .prepareSuccessMessage(COMMENT_SERVICE.deleteComment(id));
    }
}