package kg.itacademy.controller;

import kg.itacademy.model.course.CourseModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.model.UserCourseMappingModel;
import kg.itacademy.service.UserCourseMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buy-course")
@RequiredArgsConstructor
public class UserCourseMappingController {

    private final UserCourseMappingService USER_COURSE_MAPPING_SERVICE;

    @PostMapping("/create-by-course-id/{courseId}")
    public ResponseMessage<UserCourseMappingModel> create(@PathVariable Long courseId) {
        return new ResponseMessage<UserCourseMappingModel>()
                .prepareSuccessMessage(USER_COURSE_MAPPING_SERVICE.createByCourseId(courseId));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<UserCourseMappingModel> getById(@PathVariable Long id) {
        return new ResponseMessage<UserCourseMappingModel>()
                .prepareSuccessMessage(USER_COURSE_MAPPING_SERVICE.getUserCourseMappingModelById(id));
    }

    @GetMapping("/get-all-purchased-curses/{userId}")
    public ResponseMessage<List<CourseModel>> getAllPurchasedCourses(@PathVariable Long userId) {
        return new ResponseMessage<List<CourseModel>>()
                .prepareSuccessMessage(USER_COURSE_MAPPING_SERVICE.getAllPurchasedCourses(userId));
    }
}