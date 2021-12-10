package kg.itacademy.controller;

import kg.itacademy.model.CourseModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.model.UserCourseMappingModel;
import kg.itacademy.service.UserCourseMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buy-course")
public class UserCourseMappingController {

    @Autowired
    private UserCourseMappingService userCourseMappingService;

    @PostMapping("/create-by-course-id/{courseId}")
    public ResponseMessage<UserCourseMappingModel> create(@PathVariable Long courseId) {
        return new ResponseMessage<UserCourseMappingModel>()
                .prepareSuccessMessage(userCourseMappingService.createByCourseId(courseId));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<UserCourseMappingModel> getById(@PathVariable Long id) {
        return new ResponseMessage<UserCourseMappingModel>()
                .prepareSuccessMessage(userCourseMappingService.getUserCourseMappingModelById(id));
    }

    @GetMapping("/get-all-purchased-curses/{userId}")
    public ResponseMessage<List<CourseModel>> getAllPurchasedCourses(@PathVariable Long userId) {
        return new ResponseMessage<List<CourseModel>>()
                .prepareSuccessMessage(userCourseMappingService.getAllPurchasedCourses(userId));
    }
}