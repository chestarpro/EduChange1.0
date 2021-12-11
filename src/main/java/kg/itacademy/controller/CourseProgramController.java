package kg.itacademy.controller;

import kg.itacademy.model.courseProgram.CourseProgramModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.service.CourseProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-program")
@RequiredArgsConstructor
public class CourseProgramController {

    private final CourseProgramService courseProgramService;

    @PostMapping("/create")
    public ResponseMessage<CourseProgramModel> save(@RequestBody CourseProgramModel courseProgramModel) {
        return new ResponseMessage<CourseProgramModel>()
                .prepareSuccessMessage(courseProgramService.createCourseProgram(courseProgramModel));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<CourseProgramModel> getById(@PathVariable Long id) {
        return new ResponseMessage<CourseProgramModel>()
                .prepareSuccessMessage(courseProgramService.getCourseProgramModelById(id));
    }

    @GetMapping("/get-all/by-course-id/{courseId}")
    public ResponseMessage<List<CourseProgramModel>> getAllByCourseId(@PathVariable Long courseId) {
        return new ResponseMessage<List<CourseProgramModel>>()
                .prepareSuccessMessage(courseProgramService.getAllCourseProgramModelByCourseId(courseId));
    }

    @PutMapping("/update")
    public ResponseMessage<CourseProgramModel> update(@RequestBody CourseProgramModel courseProgramModel) {
        return new ResponseMessage<CourseProgramModel>()
                .prepareSuccessMessage(courseProgramService.updateCurseProgram(courseProgramModel));
    }
}