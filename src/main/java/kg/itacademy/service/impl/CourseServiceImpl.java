package kg.itacademy.service.impl;

import kg.itacademy.entity.Course;
import kg.itacademy.repository.CourseRepository;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final UserService userService;

    @Override
    public Course create(Course course) {
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
    public Course getById(Long id) {
        return courseRepository.getById(id);
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course update(Course course) {
        Course updateCourse = courseRepository.findById(course.getId()).orElse(null);
        if (course.getPrice().doubleValue() < 0) {
            throw new IllegalArgumentException("Не корректная цена курса");
        }
        if (updateCourse != null) {
            return courseRepository.save(updateCourse);
        }

        return null;
    }

    @Override
    public List<Course> findAllByCourseName(String courseName) {
        return courseRepository.findAllByCourseName(courseName);
    }

    @Override
    public List<Course> findAllByCourseCategoryName(String categoryName) {
        return courseRepository.findAllByCategory_CategoryName(categoryName);
    }

    @Override
    public List<Course> findAllByCategoryId(Long id) {
        return courseRepository.findAllByCategory_Id(id);
    }

    @Override
    public List<Course> findAllCreatedCourses() {
        return courseRepository.findAllByUser_Id(userService.getCurrentUser().getId());
    }

    @Override
    public Course deleteCourseById(Long id) {
        Course deleteCourse = courseRepository.findById(id).orElse(null);
        if (deleteCourse == null)
            throw new IllegalArgumentException("ID курса не существует");
        courseRepository.delete(deleteCourse);

        return deleteCourse;
    }
}
