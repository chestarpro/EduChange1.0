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
        validateEmail(course);
        validateLengthVariables(course);
        validateVariablesForNullOrIsEmpty(course);
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
            throw new ApiFailException("Курс под id: " + id + " не найден");
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
        validateEmail(course);
        validateLengthVariablesForUpdate(course);
        validateVariablesForNullOrIsEmptyUpdate(course);
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
    public List<CourseModel> getAllByUserId() {
        List<Course> courses = courseRepository
                .findAllByUser_Id(userService.getCurrentUser().getId());
        List<CourseModel> courseModels = new ArrayList<>();
        for (Course course : courses) {
            courseModels.add(new CourseConverter().convertFromEntity(course));
        }
        return courseModels;
    }

    @Override
    public CourseModel deleteCourseById(Long id) {
        Course deleteCourse = courseRepository.findById(id).orElse(null);
        if (deleteCourse == null)
            throw new ApiFailException("ID курса не существует");
        courseRepository.delete(deleteCourse);

        return new CourseConverter().convertFromEntity(deleteCourse);
    }

    private void validateEmail(Course course) {
        if (course.getEmail() != null)
            userService.validateEmail(course.getEmail());
    }

    @Override
    public void validateLengthVariables(Course course) {
        if (course.getCourseShortInfo().length() > 50)
            throw new ApiFailException("Превышен лимит 50 символов");
        if (course.getCourseInfoTitle().length() > 50)
            throw new ApiFailException("Превышен лимит 50 символов");
    }

    @Override
    public void validateLengthVariablesForUpdate(Course course) {
        if (course.getCourseShortInfo() != null && course.getCourseShortInfo().length() > 50)
            throw new ApiFailException("Превышен лимит 50 символов");

        if (course.getCourseInfoTitle() != null && course.getCourseInfoTitle().length() > 50)
            throw new ApiFailException("Превышен лимит 50 символов");
    }

    @Override
    public void validateVariablesForNullOrIsEmpty(Course course) {
        if (course.getCategory() == null)
            throw new ApiFailException("Не выбрана категория");

        if (course.getCourseName() == null || course.getCourseName().isEmpty())
            throw new ApiFailException("Нет названия курса");

        if (course.getPhoneNumber() == null || course.getPhoneNumber().isEmpty())
            throw new ApiFailException("Не указан рабочий номер телефона");

        if (course.getCourseShortInfo() == null || course.getCourseShortInfo().isEmpty())
            throw new ApiFailException("Нет короткой описании курса");

        if (course.getCourseInfoTitle() == null || course.getCourseInfoTitle().isEmpty())
            throw new ApiFailException("Нет заголовка описания урока");

        if (course.getCourseInfo() == null || course.getCourseInfo().isEmpty())
            throw new ApiFailException("Нет описания курса");

        if (course.getPrice().doubleValue() < 0) {
            throw new ApiFailException("Не корректная цена курса");
        }
    }

    @Override
    public void validateVariablesForNullOrIsEmptyUpdate(Course course) {
        if (course.getCourseName() != null && course.getCourseName().isEmpty())
            throw new ApiFailException("Нет названия курса");

        if (course.getPhoneNumber() != null && course.getPhoneNumber().isEmpty())
            throw new ApiFailException("Нет указан рабочий номер телефона");

        if (course.getCourseShortInfo() != null && course.getCourseShortInfo().isEmpty())
            throw new ApiFailException("Нет короткой описании курса");

        if (course.getCourseInfoTitle() != null && course.getCourseInfoTitle().isEmpty())
            throw new ApiFailException("Нет заголовка описания урока");

        if (course.getCourseInfo() != null && course.getCourseInfo().isEmpty())
            throw new ApiFailException("Нет описания курса");

        if (course.getPrice() != null && course.getPrice().doubleValue() < 0) {
            throw new ApiFailException("Не корректная цена курса");
        }
    }
}