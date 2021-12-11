package kg.itacademy.controller;

import kg.itacademy.model.lesson.LessonModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/create")
    public ResponseMessage<LessonModel> save(@RequestBody LessonModel lessonModel) {
        return new ResponseMessage<LessonModel>()
                .prepareSuccessMessage(lessonService.createLesson(lessonModel));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<LessonModel> getById(@PathVariable Long id) {
        return new ResponseMessage<LessonModel>()
                .prepareSuccessMessage(lessonService.getLessonModelById(id));
    }

    @GetMapping("/get-all/by-course-id/{id}")
    public ResponseMessage<List<LessonModel>> getAllByCourseId(@PathVariable Long id) {
        return new ResponseMessage<List<LessonModel>>()
                .prepareSuccessMessage(lessonService.getAllByCourseId(id));
    }

    @PutMapping("/update")
    public ResponseMessage<LessonModel> update(@RequestBody LessonModel lessonModel) {
        return new ResponseMessage<LessonModel>()
                .prepareSuccessMessage(lessonService.updateLesson(lessonModel));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseMessage<LessonModel> delete(@PathVariable Long id) {
        return new ResponseMessage<LessonModel>()
                .prepareSuccessMessage(lessonService.deleteLessonById(id));
    }
}