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

    private final LikeService likeService;

    @PostMapping("/create/{courseId}")
    public ResponseMessage<LikeModel> save(@PathVariable Long courseId) {
        return new ResponseMessage<LikeModel>()
                .prepareSuccessMessage(likeService.createLikeByCourseId(courseId));
    }

    @GetMapping("/get-All/by-course-id/{courseId}")
    public ResponseMessage<List<LikeModel>> getAllByCourseId(@PathVariable Long courseId) {
        return new ResponseMessage<List<LikeModel>>()
                .prepareSuccessMessage(likeService.getAllLikeModelByCourseId(courseId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseMessage<LikeModel> delete(@PathVariable Long id) {
        return new ResponseMessage<LikeModel>()
                .prepareSuccessMessage(likeService.deleteLike(id));
    }
}
