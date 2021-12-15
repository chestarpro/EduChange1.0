package kg.itacademy.controller;

import kg.itacademy.model.CourseImageModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.service.CourseImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/course-image")
@RequiredArgsConstructor
public class CourseImageController {
    private final CourseImageService COURSE_IMAGE_SERVICE;

    @PostMapping("/create/{courseId}")
    public ResponseMessage<CourseImageModel> save(@RequestParam(name = "file") MultipartFile file,
                                                  @PathVariable Long courseId) {
        return new ResponseMessage<CourseImageModel>()
                .prepareSuccessMessage(COURSE_IMAGE_SERVICE.createCourseImage(file, courseId));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<CourseImageModel> getById(@PathVariable Long id) {
        return new ResponseMessage<CourseImageModel>()
                .prepareSuccessMessage(COURSE_IMAGE_SERVICE.getCourseImageModelById(id));
    }

    @GetMapping("/get-by-course-id/{courseId}")
    public ResponseMessage<CourseImageModel> getByCourseId(@PathVariable Long courseId) {
        return new ResponseMessage<CourseImageModel>()
                .prepareSuccessMessage(COURSE_IMAGE_SERVICE.getCourseImageModelByCourseId(courseId));
    }

    @PutMapping("/update/{id}")
    public ResponseMessage<CourseImageModel> update(@RequestParam(name = "file") MultipartFile multipartFile,
                                                    @PathVariable Long id) {
        return new ResponseMessage<CourseImageModel>()
                .prepareSuccessMessage(COURSE_IMAGE_SERVICE.updateImage(multipartFile, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseMessage<CourseImageModel> delete(@PathVariable Long id) {
        return new ResponseMessage<CourseImageModel>()
                .prepareSuccessMessage(COURSE_IMAGE_SERVICE.deleteImage(id));
    }
}