package kg.itacademy.controller;

import kg.itacademy.converter.CourseConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.model.CourseModel;
import kg.itacademy.model.ResponseMessage;
import kg.itacademy.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/create")
    public ResponseMessage<CourseModel> saveCourse(@RequestBody CourseModel courseModel) {
        ResponseMessage<CourseModel> responseMessage = new ResponseMessage<>();
        try {
            Course course = new CourseConverter().convertFromModel(courseModel);
            return responseMessage
                    .prepareSuccessMessage(new CourseConverter()
                            .convertFromEntity(courseService.create(course)));
        } catch (IllegalArgumentException e) {
            return responseMessage.prepareFailMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return responseMessage.prepareFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseMessage<CourseModel> updateCourse(@RequestBody CourseModel courseModel) {
        ResponseMessage<CourseModel> responseMessage = new ResponseMessage<>();
        try {
            Course course = new CourseConverter().convertFromModel(courseModel);
            return responseMessage.prepareSuccessMessage(new CourseConverter()
                    .convertFromEntity(courseService.update(course)));
        } catch (IllegalArgumentException e) {
            return responseMessage.prepareFailMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return responseMessage.prepareFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @GetMapping("/get-all")
    public List<Course> getAll() {
        return courseService.getAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseMessage<CourseModel> delete(@PathVariable Long id) {
        ResponseMessage<CourseModel> responseMessage = new ResponseMessage<>();
        try {
            return responseMessage
                    .prepareSuccessMessage(new CourseConverter()
                            .convertFromEntity(courseService.deleteCourseById(id)));
        } catch (IllegalArgumentException e) {
            return responseMessage.prepareFailMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return responseMessage.prepareFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}