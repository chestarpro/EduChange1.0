package kg.itacademy.service.impl;

import kg.itacademy.entity.Course;
import kg.itacademy.repository.CategoryRepository;
import kg.itacademy.repository.CourseRepository;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;

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
    public List<Course> findAllMyCourse() {
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
