package kg.itacademy.controller;

import kg.itacademy.model.courseProgram.CourseProgramModel;
import kg.itacademy.model.courseProgram.CreateCourseProgramModel;
import kg.itacademy.model.courseProgram.UpdateCourseProgramModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.service.CourseProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-program")
@RequiredArgsConstructor
public class CourseProgramController {
    private final CourseProgramService COURSE_PROGRAM_SERVICE;

    @PostMapping("/create")
    public ResponseMessage<CourseProgramModel> save(@RequestBody CreateCourseProgramModel createCourseProgramModel) {
        return new ResponseMessage<CourseProgramModel>()
                .prepareSuccessMessage(COURSE_PROGRAM_SERVICE.createCourseProgram(createCourseProgramModel));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<CourseProgramModel> getById(@PathVariable Long id) {
        return new ResponseMessage<CourseProgramModel>()
                .prepareSuccessMessage(COURSE_PROGRAM_SERVICE.getCourseProgramModelById(id));
    }

    @GetMapping("/get-all/by-course-id/{courseId}")
    public ResponseMessage<List<CourseProgramModel>> getAllByCourseId(@PathVariable Long courseId) {
        return new ResponseMessage<List<CourseProgramModel>>()
                .prepareSuccessMessage(COURSE_PROGRAM_SERVICE.getAllCourseProgramModelByCourseId(courseId));
    }

    @PutMapping("/update")
    public ResponseMessage<CourseProgramModel> update(@RequestBody UpdateCourseProgramModel updateCourseProgramModel) {
        return new ResponseMessage<CourseProgramModel>()
                .prepareSuccessMessage(COURSE_PROGRAM_SERVICE.updateCurseProgram(updateCourseProgramModel));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseMessage<CourseProgramModel> delete(@PathVariable Long id) {
        return new ResponseMessage<CourseProgramModel>()
                .prepareSuccessMessage(COURSE_PROGRAM_SERVICE.deleteCourseProgram(id));
    }
}