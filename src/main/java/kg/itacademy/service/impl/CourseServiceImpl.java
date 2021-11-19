package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.Like;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.CourseModel;
import kg.itacademy.repository.CourseRepository;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.UserService;
import kg.itacademy.util.VariableValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return courseRepository.findById(id).orElse(null);
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
        CourseConverter converter = new CourseConverter();
        return getAll().stream()
                .map(converter::convertFromEntity)
                .collect(Collectors.toList());
    }

    public Course update(Course course) {
        if (course.getId() == null)
            throw new ApiFailException("Course id not specified");
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
        List<Course> courses = courseRepository.findAllByCourseName(courseName);
        if (!courses.isEmpty()) {
            CourseConverter converter = new CourseConverter();
            return courses.stream()
                    .map(converter::convertFromEntity)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CourseModel> getAllByCourseCategoryName(String categoryName) {
        List<Course> courses = courseRepository.findAllByCategoryName(categoryName);
        if (!courses.isEmpty()) {
            CourseConverter converter = new CourseConverter();
            return courses.stream()
                    .map(converter::convertFromEntity)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CourseModel> getAllByCategoryId(Long id) {
        List<Course> courses = courseRepository.findAllByCategory_Id(id);
        if (!courses.isEmpty()) {
            CourseConverter converter = new CourseConverter();
            return courses.stream()
                    .map(converter::convertFromEntity)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CourseModel> getAllByUserId(Long userId) {
        List<Course> courses = courseRepository.findAllByUser_Id(userId);
        if (!courses.isEmpty()) {
            CourseConverter converter = new CourseConverter();
            return courses.stream()
                    .map(converter::convertFromEntity)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public CourseModel deleteCourseById(Long id) {
        Course deleteCourse = getById(id);

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
            throw new ApiFailException("Exceeded character limit (50) for short ifo");
        if (course.getCourseInfoTitle().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for title ifo");
    }

    @Override
    public void validateLengthVariablesForUpdate(Course course) {
        if (course.getCourseShortInfo() != null && course.getCourseShortInfo().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for short ifo");

        if (course.getCourseInfoTitle() != null && course.getCourseInfoTitle().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for title ifo");
    }

    @Override
    public void validateVariablesForNullOrIsEmpty(Course course) {
        if (course.getCategory() == null)
            throw new ApiFailException("Category is not filled");

        if (course.getCourseName() == null || course.getCourseName().isEmpty())
            throw new ApiFailException("Course name is not filled");

        if (course.getPhoneNumber() == null || course.getPhoneNumber().isEmpty())
            throw new ApiFailException("Phone number is not filled");

        if (course.getCourseShortInfo() == null || course.getCourseShortInfo().isEmpty())
            throw new ApiFailException("Short info is not filled");

        if (course.getCourseInfoTitle() == null || course.getCourseInfoTitle().isEmpty())
            throw new ApiFailException("Title info is not filled");

        if (course.getCourseInfo() == null || course.getCourseInfo().isEmpty())
            throw new ApiFailException("Course info is not filled");

        if (course.getPrice().doubleValue() < 0) {
            throw new ApiFailException("Wrong balance format");
        }
    }

    @Override
    public void validateVariablesForNullOrIsEmptyUpdate(Course course) {
        if (course.getCategory() != null && course.getCategory().getId() == null)
            throw new ApiFailException("Category is not filled");

        if (course.getCourseName() != null && course.getCourseName().isEmpty())
            throw new ApiFailException("Course name is not filled");

        if (course.getPhoneNumber() != null && course.getPhoneNumber().isEmpty())
            throw new ApiFailException("Phone number is not filled");

        if (course.getCourseShortInfo() != null && course.getCourseShortInfo().isEmpty())
            throw new ApiFailException("Short info is not filled");

        if (course.getCourseInfoTitle() != null && course.getCourseInfoTitle().isEmpty())
            throw new ApiFailException("Title info is not filled");

        if (course.getCourseInfo() != null && course.getCourseInfo().isEmpty())
            throw new ApiFailException("Course info is not filled");

        if (course.getPrice() != null && course.getPrice().doubleValue() < 0) {
            throw new ApiFailException("Wrong balance format");
        }
    }
}