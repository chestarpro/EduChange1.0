package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.course.CourseModel;
import kg.itacademy.repository.CourseRepository;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.UserService;
import kg.itacademy.util.VariableValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService, VariableValidation<Course> {

    private final CourseRepository COURSE_REPOSITORY;
    private final UserService USER_SERVICE;
    private final CourseConverter COURSE_CONVERTER;

    @Override
    public Course save(Course course) {
        validateEmail(course);
        validateLengthVariables(course);
        validateVariablesForNullOrIsEmpty(course);
        course.setUser(USER_SERVICE.getCurrentUser());
        return COURSE_REPOSITORY.save(course);
    }

    @Override
    public CourseModel createCourse(CourseModel courseModel) {
        courseModel.setCourseName(courseModel.getCourseName().toLowerCase(Locale.ROOT));
        Course course = save(COURSE_CONVERTER.convertFromModel(courseModel));
        return COURSE_CONVERTER.convertFromEntity(course);
    }

    @Override
    public Course getById(Long id) {
        return COURSE_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public CourseModel getCourseModelById(Long id) {
        return COURSE_CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public List<Course> getAll() {
        return COURSE_REPOSITORY.findAll();
    }

    @Override
    public List<CourseModel> getAllCourseModel() {
        return getAll().stream()
                .map(COURSE_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    public Course update(Course course) {
        if (course.getId() == null)
            throw new ApiFailException("Course id not specified");
        validateEmail(course);
        validateLengthVariablesForUpdate(course);
        validateVariablesForNullOrIsEmptyUpdate(course);
        return COURSE_REPOSITORY.save(course);
    }

    @Override
    public CourseModel updateCourse(CourseModel courseModel) {
        update(COURSE_CONVERTER.convertFromModel(courseModel));
        return courseModel;
    }

    @Override
    public List<CourseModel> getAllByCourseName(String courseName) {
        return COURSE_REPOSITORY.findAllByCourseName(courseName.toLowerCase(Locale.ROOT))
                .stream()
                .map(COURSE_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseModel> getAllByCourseCategoryName(String categoryName) {
        return COURSE_REPOSITORY.findAllByCategoryName(categoryName.toLowerCase(Locale.ROOT))
                .stream()
                .map(COURSE_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseModel> getAllByCategoryId(Long id) {
        return COURSE_REPOSITORY.findAllByCategory_Id(id).stream()
                .map(COURSE_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseModel> getAllByUserId(Long userId) {
        return COURSE_REPOSITORY.findAllByUser_Id(userId).stream()
                .map(COURSE_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CourseModel deleteCourseById(Long id) {
        Course deleteCourse = getById(id);

        if (deleteCourse == null)
            throw new ApiFailException("Course by user id " + id + " not found");

        COURSE_REPOSITORY.delete(deleteCourse);
        return COURSE_CONVERTER.convertFromEntity(deleteCourse);
    }

    private void validateEmail(Course course) {
        if (course.getEmail() != null)
            USER_SERVICE.validateEmail(course.getEmail());
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