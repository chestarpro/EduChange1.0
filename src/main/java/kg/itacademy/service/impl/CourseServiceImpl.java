package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseConverter;
import kg.itacademy.entity.Category;
import kg.itacademy.entity.Course;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.course.*;
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
public class CourseServiceImpl implements CourseService {

    private final CourseRepository COURSE_REPOSITORY;
    @Autowired
    private UserService USER_SERVICE;
    private final CourseImageService COURSE_IMAGE_SERVICE;
    private final CourseConverter COURSE_CONVERTER;
    @Autowired
    private CourseProgramService PROGRAM_SERVICE;
    private final RegexUtil REGEX_UTIL;
    @Autowired
    private CommentService COMMENT_SERVICE;
    @Autowired
    private LikeService LIKE_SERVICE;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CategoryService CATEGORY_SERVICE;

    @Override
    public Course save(Course course) {
        return COURSE_REPOSITORY.save(course);
    }

    @Override
    public CourseDataModel createCourse(CreateCourseModel createCourseModel) {

        validateEmailAndPhoneNumber(createCourseModel);
        validateLengthVariables(createCourseModel);
        validateVariablesForNullOrIsEmpty(createCourseModel);

        Course course = new Course();
        Category category = new Category();
        category.setId(createCourseModel.getCategoryId());

        course.setCategory(category);
        course.setCourseName(createCourseModel.getCourseName().toLowerCase(Locale.ROOT));
        course.setCourseShortInfo(createCourseModel.getCourseShortInfo());
        course.setCourseInfo(createCourseModel.getCourseInfo());
        course.setCourseInfoTitle(createCourseModel.getCourseInfoTitle());
        course.setCourseInfoUrl(createCourseModel.getCourseInfoUrl());
        course.setPhoneNumber(createCourseModel.getPhoneNumber());
        course.setEmail(createCourseModel.getEmail());
        course.setPrice(createCourseModel.getPrice());
        course.setUser(USER_SERVICE.getCurrentUser());

        COURSE_REPOSITORY.save(course);

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


    @Override
    public CourseModel updateCourse(UpdateCourseModel updateCourseModel) {
        Long courseId = updateCourseModel.getId();

        if (courseId == null)
            throw new ApiFailException("Course id not specified");

        Course dataCourse = getById(courseId);

        if (dataCourse == null)
            throw new ApiFailException("Course by id " + courseId + " not found");

        validateEmailAndPhoneNumber(updateCourseModel);
        validateLengthVariablesForUpdate(updateCourseModel);
        validateVariablesForNullOrIsEmptyUpdate(updateCourseModel);

        setForUpdateCourse(dataCourse, updateCourseModel);

        COURSE_REPOSITORY.save(dataCourse);

        return COURSE_CONVERTER.convertFromEntity(dataCourse);
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

    private void validateEmailAndPhoneNumber(CreateCourseModel createCourseModel) {
        if (createCourseModel.getEmail() != null)
            REGEX_UTIL.validateEmail(createCourseModel.getEmail());
        if (createCourseModel.getPhoneNumber() != null) {
            REGEX_UTIL.validatePhoneNumber(createCourseModel.getPhoneNumber());
        }
    }

    private void validateEmailAndPhoneNumber(UpdateCourseModel updateCourseModel) {
        if (updateCourseModel.getEmail() != null)
            REGEX_UTIL.validateEmail(updateCourseModel.getEmail());
        if (updateCourseModel.getPhoneNumber() != null) {
            REGEX_UTIL.validatePhoneNumber(updateCourseModel.getPhoneNumber());
        }
    }

    public void validateLengthVariables(CreateCourseModel createCourseModel) {
        if (createCourseModel.getCourseShortInfo().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for short info");
        if (createCourseModel.getCourseInfoTitle().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for title info");
    }


    public void validateLengthVariablesForUpdate(UpdateCourseModel updateCourseModel) {
        if (updateCourseModel.getCourseShortInfo() != null && updateCourseModel.getCourseShortInfo().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for short info");

        if (updateCourseModel.getCourseInfoTitle() != null && updateCourseModel.getCourseInfoTitle().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for title info");
    }

    public void validateVariablesForNullOrIsEmpty(CreateCourseModel createCourseModel) {
        Long categoryId = createCourseModel.getCategoryId();

        if (categoryId == null)
            throw new ApiFailException("Category is not filled");
        else {
            Category category = CATEGORY_SERVICE.getById(categoryId);
            if (category == null)
                throw new ApiFailException("Category by id " + categoryId + " not found");
        }

        if (createCourseModel.getCourseName() == null || createCourseModel.getCourseName().isEmpty())
            throw new ApiFailException("Course name is not filled");

        if (createCourseModel.getPhoneNumber() == null || createCourseModel.getPhoneNumber().isEmpty())
            throw new ApiFailException("Phone number is not filled");

        if (createCourseModel.getCourseShortInfo() == null || createCourseModel.getCourseShortInfo().isEmpty())
            throw new ApiFailException("Short info is not filled");

        if (createCourseModel.getCourseInfoTitle() == null || createCourseModel.getCourseInfoTitle().isEmpty())
            throw new ApiFailException("Title info is not filled");

        if (createCourseModel.getCourseInfo() == null || createCourseModel.getCourseInfo().isEmpty())
            throw new ApiFailException("Course info is not filled");

        if (createCourseModel.getPrice().doubleValue() < 0) {
            throw new ApiFailException("Wrong balance format");
        }
    }

    public void validateVariablesForNullOrIsEmptyUpdate(UpdateCourseModel updateCourseModel) {
        Long categoryId = updateCourseModel.getCategoryId();
        if (categoryId != null) {
            Category category = CATEGORY_SERVICE.getById(categoryId);
            if (category == null)
                throw new ApiFailException("Category by id " + categoryId + " not found");
        }
        if (updateCourseModel.getCourseName() != null && updateCourseModel.getCourseName().isEmpty())
            throw new ApiFailException("Course name is not filled");

        if (updateCourseModel.getPhoneNumber() != null && updateCourseModel.getPhoneNumber().isEmpty())
            throw new ApiFailException("Phone number is not filled");

        if (updateCourseModel.getCourseShortInfo() != null && updateCourseModel.getCourseShortInfo().isEmpty())
            throw new ApiFailException("Short info is not filled");

        if (updateCourseModel.getCourseInfoTitle() != null && updateCourseModel.getCourseInfoTitle().isEmpty())
            throw new ApiFailException("Title info is not filled");

        if (updateCourseModel.getCourseInfo() != null && updateCourseModel.getCourseInfo().isEmpty())
            throw new ApiFailException("Course info is not filled");

        if (updateCourseModel.getPrice() != null && updateCourseModel.getPrice().doubleValue() < 0) {
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

    private void setForUpdateCourse(Course course, UpdateCourseModel updateCourseModel) {
        if (updateCourseModel.getCategoryId() != null) {
            Category category = new Category();
            category.setId(updateCourseModel.getCategoryId());
            course.setCategory(category);
        }

        if (updateCourseModel.getCourseName() != null)
            course.setCourseName(updateCourseModel.getCourseName());

        if (updateCourseModel.getCourseShortInfo() != null)
            course.setCourseShortInfo(updateCourseModel.getCourseShortInfo());

        if (updateCourseModel.getCourseInfoTitle() != null)
            course.setCourseInfoTitle(updateCourseModel.getCourseInfoTitle());

        if (updateCourseModel.getCourseInfo() != null)
            course.setCourseInfo(updateCourseModel.getCourseInfo());

        if (updateCourseModel.getCourseInfoUrl() != null)
            course.setCourseInfoUrl(updateCourseModel.getCourseInfoUrl());

        if (updateCourseModel.getPhoneNumber() != null)
            course.setPhoneNumber(updateCourseModel.getPhoneNumber());

        if (updateCourseModel.getEmail() != null)
            course.setEmail(updateCourseModel.getEmail());

        if (updateCourseModel.getPrice() != null)
            course.setPrice(updateCourseModel.getPrice());
    }
}