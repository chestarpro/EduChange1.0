package kg.itacademy.controller;

import kg.itacademy.model.CourseImageModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.service.CourseImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/course-image")
public class CourseImageController {

    @Autowired
    private CourseImageService courseImageService;

    @PostMapping("/create/{courseId}")
    public ResponseMessage<CourseImageModel> save(@RequestParam(name = "file") MultipartFile file,
                                                  @PathVariable Long courseId) {
        return new ResponseMessage<CourseImageModel>()
                .prepareSuccessMessage(courseImageService.createCourseImage(file, courseId));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<CourseImageModel> getById(@PathVariable Long id) {
        return new ResponseMessage<CourseImageModel>()
                .prepareSuccessMessage(courseImageService.getCourseImageModelById(id));
    }

    @GetMapping("/get-by-course-id/{courseId}")
    public ResponseMessage<CourseImageModel> getAllByCourseId(@PathVariable Long courseId) {
        return new ResponseMessage<CourseImageModel>()
                .prepareSuccessMessage(courseImageService.getCourseImageModelByCourseId(courseId));
    }

    @PutMapping("/update/{id}")
    public ResponseMessage<CourseImageModel> update(@RequestParam(name = "file") MultipartFile multipartFile,
                                                    @PathVariable Long id) {
        return new ResponseMessage<CourseImageModel>()
                .prepareSuccessMessage(courseImageService.updateImage(multipartFile, id));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseMessage<CourseImageModel> delete(@PathVariable Long id) {
        return new ResponseMessage<CourseImageModel>()
                .prepareSuccessMessage(courseImageService.deleteImage(id));
    }
}