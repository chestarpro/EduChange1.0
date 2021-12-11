package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.course.CourseDataModel;
import kg.itacademy.model.course.CourseModel;
import kg.itacademy.repository.CourseRepository;
import kg.itacademy.repository.LessonRepository;
import kg.itacademy.service.*;
import kg.itacademy.util.RegexUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseRepository COURSE_REPOSITORY;
    private final UserService USER_SERVICE;
    private final CourseImageService COURSE_IMAGE_SERVICE;
    private final CourseConverter COURSE_CONVERTER;
    private final CourseProgramService PROGRAM_SERVICE;
    private final RegexUtil REGEX_UTIL;
    @Autowired
    private  CommentService COMMENT_SERVICE;
    @Autowired
    private LikeService LIKE_SERVICE;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public Course save(Course course) {
        validateEmailAndPhoneNumber(course);
        validateLengthVariables(course);
        validateVariablesForNullOrIsEmpty(course);
        course.setUser(USER_SERVICE.getCurrentUser());
        return COURSE_REPOSITORY.save(course);
    }

    @Override
    public CourseDataModel createCourse(CourseModel courseModel) {
        courseModel.setCourseName(courseModel.getCourseName().toLowerCase(Locale.ROOT));
        Course course = save(COURSE_CONVERTER.convertFromModel(courseModel));

        return getCourseDataModelByCourseId(course.getId());
    }

    @Override
    public Course getById(Long id) {
        return COURSE_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public CourseDataModel getCourseModelById(Long id) {
        return getCourseDataModelByCourseId(id);
    }

    @Override
    public List<Course> getAll() {
        return COURSE_REPOSITORY.findAll();
    }

    @Override
    public List<CourseDataModel> getAllCourseModel() {
        return getAll()
                .stream()
                .map(i -> getCourseDataModelByCourseId(i.getId()))
                .collect(Collectors.toList());
    }

    public Course update(Course course) {
        if (course.getId() == null)
            throw new ApiFailException("Course id not specified");
        validateEmailAndPhoneNumber(course);
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
    public List<CourseDataModel> getAllByCourseName(String courseName) {

      return COURSE_REPOSITORY.findAllByCourseName(courseName.toLowerCase(Locale.ROOT))
                .stream()
                .map(i -> getCourseDataModelByCourseId(i.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDataModel> getAllByCourseCategoryName(String categoryName) {
        return COURSE_REPOSITORY.findAllByCategoryName(categoryName.toLowerCase(Locale.ROOT))
                .stream()
                .map(i -> getCourseDataModelByCourseId(i.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDataModel> getAllByCategoryId(Long id) {
       return COURSE_REPOSITORY.findAllByCategory_Id(id)
                .stream()
                .map(i -> getCourseDataModelByCourseId(i.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDataModel> getAllByUserId(Long userId) {
        return COURSE_REPOSITORY.findAllByUser_Id(userId)
                .stream()
                .map(i -> getCourseDataModelByCourseId(i.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public CourseModel deleteCourseById(Long id) {
        Course course = getById(id);

        if (course == null)
            throw new ApiFailException("Course by user id " + id + " not found");

        COURSE_REPOSITORY.deleteById(id);
        return COURSE_CONVERTER.convertFromEntity(course);
    }

    private void validateEmailAndPhoneNumber(Course course) {
        if (course.getEmail() != null)
            REGEX_UTIL.validateEmail(course.getEmail());
        if (course.getPhoneNumber() != null) {
            REGEX_UTIL.validatePhoneNumber(course.getPhoneNumber());
        }
    }


    public void validateLengthVariables(Course course) {
        if (course.getCourseShortInfo().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for short ifo");
        if (course.getCourseInfoTitle().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for title ifo");
    }


    public void validateLengthVariablesForUpdate(Course course) {
        if (course.getCourseShortInfo() != null && course.getCourseShortInfo().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for short ifo");

        if (course.getCourseInfoTitle() != null && course.getCourseInfoTitle().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for title ifo");
    }

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

    @Override
    public CourseDataModel getCourseDataModelByCourseId(Long courseId) {
        CourseDataModel courseDataModel = new CourseDataModel();
        courseDataModel.setCourseModel(COURSE_CONVERTER.convertFromEntity(getById(courseId)));
        courseDataModel.setImageModel(COURSE_IMAGE_SERVICE.getCourseImageModelByCourseId(courseId));
        courseDataModel.setPrograms(PROGRAM_SERVICE.getAllCourseProgramModelByCourseId(courseId));
        courseDataModel.setLikes(LIKE_SERVICE.getAllLikeModelByCourseId(courseId));
        courseDataModel.setComments(COMMENT_SERVICE.getAllCommentModelByCourseId(courseId));

        return courseDataModel;
    }
}