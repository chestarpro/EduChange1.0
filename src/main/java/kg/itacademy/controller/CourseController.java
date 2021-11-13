package kg.itacademy.controller;

import kg.itacademy.model.CourseModel;
import kg.itacademy.model.ResponseMessage;
import kg.itacademy.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/create")
    public ResponseMessage<CourseModel> saveCourse(@RequestBody CourseModel courseModel) {
        return new ResponseMessage<CourseModel>()
                .prepareSuccessMessage(courseService.createCourse(courseModel));
    }

    @PutMapping("/update")
    public ResponseMessage<CourseModel> updateCourse(@RequestBody CourseModel courseModel) {
        return new ResponseMessage<CourseModel>()
                .prepareSuccessMessage(courseService.updateCourse(courseModel));
    }

    @GetMapping("/get/by-id/{id}")
    public ResponseMessage<CourseModel> getById(@PathVariable Long id) {
        return new ResponseMessage<CourseModel>()
                .prepareSuccessMessage(courseService.getCourseModelById(id));
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<CourseModel>> getAll() {
        return new ResponseMessage<List<CourseModel>>()
                .prepareSuccessMessage(courseService.getAllCourseModel());
    }

    @GetMapping("get-all/by-name/{courseName}")
    public ResponseMessage<List<CourseModel>> getAllByCourseName(@PathVariable String courseName) {
        return new ResponseMessage<List<CourseModel>>()
                .prepareSuccessMessage(courseService.getAllByCourseName(courseName));
    }

    @GetMapping("/get-all/by-category-id/{id}")
    public ResponseMessage<List<CourseModel>> getAllByCourseId(@PathVariable Long id) {
        return new ResponseMessage<List<CourseModel>>()
                .prepareSuccessMessage(courseService.getAllByCategoryId(id));
    }

    @GetMapping("/get-all/by-category-name/{categoryName}")
    public ResponseMessage<List<CourseModel>> getAllByCategoryName(@PathVariable String categoryName) {
        return new ResponseMessage<List<CourseModel>>()
                .prepareSuccessMessage(courseService.getAllByCourseCategoryName(categoryName));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseMessage<CourseModel> delete(@PathVariable Long id) {
        return new ResponseMessage<CourseModel>()
                .prepareSuccessMessage(courseService.deleteCourseById(id));
    }
}