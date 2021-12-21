package kg.itacademy.controller;

import kg.itacademy.model.course.CourseDataModel;
import kg.itacademy.model.course.CourseModel;
import kg.itacademy.model.course.CreateCourseModel;
import kg.itacademy.model.course.UpdateCourseModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/create")
    public ResponseMessage<CourseDataModel> save(@RequestBody CreateCourseModel createCourseModel) {
        return new ResponseMessage<CourseDataModel>()
                .prepareSuccessMessage(courseService.createCourse(createCourseModel));
    }

    @PutMapping("/update")
    public ResponseMessage<CourseDataModel> update(@RequestBody UpdateCourseModel updateCourseModel) {
        return new ResponseMessage<CourseDataModel>()
                .prepareSuccessMessage(courseService.updateCourse(updateCourseModel));
    }

    @GetMapping("/get/by-id/{id}")
    public ResponseMessage<CourseDataModel> getById(@PathVariable Long id) {
        return new ResponseMessage<CourseDataModel>()
                .prepareSuccessMessage(courseService.getCourseModelById(id));
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<CourseDataModel>> getAll() {
        return new ResponseMessage<List<CourseDataModel>>()
                .prepareSuccessMessage(courseService.getAllCourseDataModel());
    }

    @GetMapping("/get-all/by-user-id/{userId}")
    public ResponseMessage<List<CourseDataModel>> gatAllByUserId(@PathVariable Long userId) {
        return new ResponseMessage<List<CourseDataModel>>()
                .prepareSuccessMessage(courseService.getAllCourseDataModelByUserId(userId));
    }

    @GetMapping("get-all/by-name/{courseName}")
    public ResponseMessage<List<CourseDataModel>> getAllByCourseName(@PathVariable String courseName) {
        return new ResponseMessage<List<CourseDataModel>>()
                .prepareSuccessMessage(courseService.getAllCourseDataModelByCourseName(courseName));
    }

    @GetMapping("/get-all/by-category-id/{categoryId}")
    public ResponseMessage<List<CourseDataModel>> getAllByCategoryId(@PathVariable Long categoryId) {
        return new ResponseMessage<List<CourseDataModel>>()
                .prepareSuccessMessage(courseService.getAllCourseDataModelByCategoryId(categoryId));
    }

    @GetMapping("/get-all/by-category-name/{categoryName}")
    public ResponseMessage<List<CourseDataModel>> getAllByCategoryName(@PathVariable String categoryName) {
        return new ResponseMessage<List<CourseDataModel>>()
                .prepareSuccessMessage(courseService.getAllCourseDataModelByCategoryName(categoryName));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseMessage<CourseModel> delete(@PathVariable Long id) {
        return new ResponseMessage<CourseModel>()
                .prepareSuccessMessage(courseService.deleteCourseById(id));
    }
}