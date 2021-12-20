package kg.itacademy.controller;

import kg.itacademy.model.lesson.CreateLessonModel;
import kg.itacademy.model.lesson.LessonModel;
import kg.itacademy.model.lesson.UpdateLessonModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService LESSON_SERVICE;

    @PostMapping("/create")
    public ResponseMessage<LessonModel> save(@RequestBody CreateLessonModel createLessonModel) {
        return new ResponseMessage<LessonModel>()
                .prepareSuccessMessage(LESSON_SERVICE.createLesson(createLessonModel));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<LessonModel> getById(@PathVariable Long id) {
        return new ResponseMessage<LessonModel>()
                .prepareSuccessMessage(LESSON_SERVICE.getLessonModelById(id));
    }

    @GetMapping("/get-first-three/by-course-id/{courseId}")
    public ResponseMessage<List<LessonModel>> getFirstThreeLessonsByCourseId(@PathVariable Long courseId) {
        return new ResponseMessage<List<LessonModel>>()
                .prepareSuccessMessage(LESSON_SERVICE.getFirstThreeLessonsByCourseId(courseId));
    }

    @GetMapping("/get-all/by-course-id/{courseId}")
    public ResponseMessage<List<LessonModel>> getAllByCourseId(@PathVariable Long courseId) {
        return new ResponseMessage<List<LessonModel>>()
                .prepareSuccessMessage(LESSON_SERVICE.getAllByCourseId(courseId));
    }

    @GetMapping("/get-count-lesson/by-course_id/{courseId}")
    public ResponseMessage<Long> getCountLessonByCourseId(@PathVariable Long courseId) {
        return new ResponseMessage<Long>()
                .prepareSuccessMessage(LESSON_SERVICE.getCountLessonByCourseId(courseId));
    }

    @PutMapping("/update")
    public ResponseMessage<LessonModel> update(@RequestBody UpdateLessonModel updateLessonModel) {
        return new ResponseMessage<LessonModel>()
                .prepareSuccessMessage(LESSON_SERVICE.updateLesson(updateLessonModel));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseMessage<LessonModel> delete(@PathVariable Long id) {
        return new ResponseMessage<LessonModel>()
                .prepareSuccessMessage(LESSON_SERVICE.deleteLessonById(id));
    }
}