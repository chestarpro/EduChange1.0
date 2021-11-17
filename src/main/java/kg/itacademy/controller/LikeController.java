package kg.itacademy.controller;

import kg.itacademy.model.LikeModel;
import kg.itacademy.model.ResponseMessage;
import kg.itacademy.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @GetMapping("/create/{courseId}")
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
