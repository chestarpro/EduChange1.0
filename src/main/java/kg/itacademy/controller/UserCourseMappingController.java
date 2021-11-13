package kg.itacademy.controller;

import kg.itacademy.converter.UserCourseMappingConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.model.ResponseMessage;
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

    @PostMapping
    public ResponseMessage<UserCourseMappingModel> create(@RequestBody UserCourseMappingModel userCourseMappingModel) {
        UserCourseMapping userCourseMapping = new UserCourseMappingConverter().convertFromModel(userCourseMappingModel);

        return new ResponseMessage<UserCourseMappingModel>()
                .prepareSuccessMessage(new UserCourseMappingConverter()
                        .convertFromEntity(userCourseMappingService.save(userCourseMapping)));

    }

    @GetMapping
    public List<Course> getAll() {
        return userCourseMappingService.findAllPurchasedCourses();
    }
}