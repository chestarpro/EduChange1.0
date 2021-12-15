package kg.itacademy.controller;

import kg.itacademy.model.LikeModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService LIKE_SERVICE;

    @PostMapping("/create/{courseId}")
    public ResponseMessage<LikeModel> save(@PathVariable Long courseId) {
        return new ResponseMessage<LikeModel>()
                .prepareSuccessMessage(LIKE_SERVICE.createLikeByCourseId(courseId));
    }

    @GetMapping("/get-all/by-course-id/{courseId}")
    public ResponseMessage<List<LikeModel>> getAllByCourseId(@PathVariable Long courseId) {
        return new ResponseMessage<List<LikeModel>>()
                .prepareSuccessMessage(LIKE_SERVICE.getAllLikeModelByCourseId(courseId));
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseMessage<LikeModel> delete(@PathVariable Long courseId) {
        return new ResponseMessage<LikeModel>()
                .prepareSuccessMessage(LIKE_SERVICE.deleteLike(courseId));
    }
}