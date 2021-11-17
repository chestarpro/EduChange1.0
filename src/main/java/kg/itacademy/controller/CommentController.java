package kg.itacademy.controller;

import kg.itacademy.model.CommentModel;
import kg.itacademy.model.CreateCommentModel;
import kg.itacademy.model.ResponseMessage;
import kg.itacademy.model.UpdateCommentModel;
import kg.itacademy.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseMessage<CommentModel> save(@RequestBody CreateCommentModel createCommentModel) {
        return new ResponseMessage<CommentModel>()
                .prepareSuccessMessage(commentService.createCommentByCourseId(createCommentModel));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<CommentModel> getById(@PathVariable Long id) {
        return new ResponseMessage<CommentModel>()
                .prepareSuccessMessage(commentService.getCommentModelById(id));
    }

    @GetMapping("/get-all/by-course-id/{id}")
    public ResponseMessage<List<CommentModel>> getAllByCourseId(@PathVariable Long id) {
        return new ResponseMessage<List<CommentModel>>()
                .prepareSuccessMessage(commentService.getAllCommentModelByCourseId(id));
    }

    @PutMapping("/update")
    public ResponseMessage<CommentModel> update(@RequestBody UpdateCommentModel updateCommentModel) {
        return new ResponseMessage<CommentModel>()
                .prepareSuccessMessage(commentService.updateByUpdateCommentModel(updateCommentModel));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseMessage<CommentModel> delete(@PathVariable Long id) {
        return new ResponseMessage<CommentModel>()
                .prepareSuccessMessage(commentService.deleteCommentById(id));
    }
}