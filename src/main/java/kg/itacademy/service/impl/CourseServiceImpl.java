package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.CourseModel;
import kg.itacademy.repository.CourseRepository;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.UserService;
import kg.itacademy.util.VariableValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService, VariableValidation<Course> {

    private final CourseRepository courseRepository;

    private final UserService userService;

    @Override
    public Course save(Course course) {
        if (course.getCategory() == null)
            throw new IllegalArgumentException("Не выбрана категория");
        if (course.getCourseName() == null)
            throw new IllegalArgumentException("Нет названия курса");
        if (course.getPhoneNumber() == null)
            throw new IllegalArgumentException("Нет указан рабочий номер телефона");
        if (course.getCourseInfo() == null)
            throw new IllegalArgumentException("Нет описания курса");
        if (course.getPrice().doubleValue() < 0) {
            throw new IllegalArgumentException("Не корректная цена курса");
        }
        course.setUser(userService.getCurrentUser());
        return courseRepository.save(course);
    }

    @Override
    public CourseModel createCourse(CourseModel courseModel) {
        save(new CourseConverter().convertFromModel(courseModel));
        return courseModel;
    }

    @Override
    public Course getById(Long id) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null)
            throw new ApiFailException("Не найден курс по id: " + id);
        return course;
    }

    @Override
    public CourseModel getCourseModelById(Long id) {
        return new CourseConverter().convertFromEntity(getById(id));
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public List<CourseModel> getAllCourseModel() {
        List<CourseModel> list = new ArrayList<>();
        for (Course course : courseRepository.findAll()) {
            list.add(new CourseConverter().convertFromEntity(course));
        }
        return list;
    }

    @Override
    public Course update(Course course) {
        if (course.getId() == null)
            throw new ApiFailException("Не указан id course");
        if (course.getPrice().doubleValue() < 0) {
            throw new IllegalArgumentException("Не корректная цена курса");
        }

        return courseRepository.save(course);
    }

    @Override
    public CourseModel updateCourse(CourseModel courseModel) {
        update(new CourseConverter().convertFromModel(courseModel));
        return courseModel;
    }

    @Override
    public List<CourseModel> getAllByCourseName(String courseName) {
        List<CourseModel> list = new ArrayList<>();
        for (Course course : courseRepository.findAllByCourseName(courseName)) {
            list.add(new CourseConverter().convertFromEntity(course));
        }
        return list;
    }

    @Override
    public List<CourseModel> getAllByCourseCategoryName(String categoryName) {
        List<CourseModel> list = new ArrayList<>();
        for (Course course : courseRepository.findAllByCategoryName(categoryName)) {
            list.add(new CourseConverter().convertFromEntity(course));
        }
        return list;
    }

    @Override
    public List<CourseModel> getAllByCategoryId(Long id) {
        List<CourseModel> list = new ArrayList<>();
        for (Course course : courseRepository.findAllByCategory_Id(id)) {
            list.add(new CourseConverter().convertFromEntity(course));
        }
        return list;
    }

    @Override
    public List<Course> findAllCreatedCourses() {
        return courseRepository.findAllByUser_Id(userService.getCurrentUser().getId());
    }

    @Override
    public CourseModel deleteCourseById(Long id) {
        Course deleteCourse = courseRepository.findById(id).orElse(null);
        if (deleteCourse == null)
            throw new IllegalArgumentException("ID курса не существует");
        courseRepository.delete(deleteCourse);

        return new CourseConverter().convertFromEntity(deleteCourse);
    }

    @Override
    public void validateLengthVariables(Course course) {

    }

    @Override
    public void validateVariablesForNullOrIsEmpty(Course course) {

    }

    @Override
    public void validateLengthVariablesForUpdateUser(Course course) {

    }

    @Override
    public void validateVariablesForNullOrIsEmptyUpdate(Course course) {

    }
}