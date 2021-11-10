package kg.itacademy.controller;

import kg.itacademy.converter.UserCourseMappingConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.model.ResponseMessage;
import kg.itacademy.model.UserCourseMappingModel;
import kg.itacademy.service.UserCourseMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mapping")
public class UserCourseMappingController {

    @Autowired
    private UserCourseMappingService userCourseMappingService;

    @PostMapping
    public ResponseMessage<UserCourseMappingModel> create(@RequestBody UserCourseMappingModel userCourseMappingModel) {
        ResponseMessage<UserCourseMappingModel> responseMessage = new ResponseMessage<>();
        UserCourseMapping userCourseMapping = new UserCourseMappingConverter().convertFromModel(userCourseMappingModel);
        try {
            return responseMessage
                    .prepareSuccessMessage(new UserCourseMappingConverter()
                            .convertFromEntity(userCourseMappingService.create(userCourseMapping)));
        } catch (IllegalArgumentException e) {
            return responseMessage.prepareFailMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping
    public List<Course> getAll() {
        return userCourseMappingService.findAllPurchasedCourses();
    }
}
