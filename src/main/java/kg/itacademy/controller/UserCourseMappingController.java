package kg.itacademy.controller;

import kg.itacademy.model.course.CourseDataModel;
import kg.itacademy.model.user.BaseUserModel;
import kg.itacademy.service.BaseService;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.model.UserCourseMappingModel;
import kg.itacademy.service.UserCourseMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class UserCourseMappingController {
    private final UserCourseMappingService userCourseMappingService;

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

    @GetMapping("/get-all-customers/by-course-id/{courseId}")
    public ResponseMessage<List<BaseUserModel>> getCustomersByCourseId(@PathVariable Long courseId) {
        return new ResponseMessage<List<BaseUserModel>>()
                .prepareSuccessMessage(userCourseMappingService.getAllCustomersByCourseId(courseId));
    }

    @GetMapping("/get-all-purchased-curses/{userId}")
    public ResponseMessage<List<CourseDataModel>> getAllPurchasedCourses(@PathVariable Long userId) {
        return new ResponseMessage<List<CourseDataModel>>()
                .prepareSuccessMessage(userCourseMappingService.getAllPurchasedCourses(userId));
    }
}