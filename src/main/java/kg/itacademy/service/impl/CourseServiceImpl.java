package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseConverter;
import kg.itacademy.entity.Category;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.course.*;
import kg.itacademy.repository.CourseRepository;
import kg.itacademy.service.*;
import kg.itacademy.util.RegexUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseImageService courseImageService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserService userService;
    @Autowired
    @Lazy
    private LessonService lessonService;
    @Autowired
    @Lazy
    private CommentService commentService;
    @Autowired
    @Lazy
    private CourseProgramService courseProgramService;
    private final CourseConverter courseConverter;
    private final CourseRepository courseRepository;

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public CourseDataModel createCourse(CreateCourseModel createCourseModel) {
        validateVariablesForNullOrIsEmpty(createCourseModel);
        validateLengthVariables(createCourseModel);
        validateEmailAndPhoneNumber(createCourseModel);

        Course course = initCourse(createCourseModel);
        courseRepository.save(course);
        return getCourseDataModelByCourseId(course.getId());
    }

    @Override
    public Course getById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Override
    public CourseDataModel getCourseModelById(Long id) {
        return getCourseDataModelByCourseId(id);
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public List<CourseDataModel> getAllCourseDataModel() {
        return getAll()
                .stream()
                .map(i -> getCourseDataModelByCourseId(i.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public CourseDataModel getCourseDataModelByCourseId(Long courseId) {
        CourseDataModel courseDataModel = new CourseDataModel();
        Course course = getById(courseId);
        if (course == null)
            throw new ApiFailException("Course not found");
        User user = userService.getById(course.getUser().getId());
        courseDataModel.setAuthorFullName(user.getFullName());
        courseDataModel.setCourseModel(courseConverter.convertFromEntity(course));
        courseDataModel.setImageModel(courseImageService.getCourseImageModelByCourseId(courseId));
        courseDataModel.setLessonCount(lessonService.getCountLessonByCourseId(courseId));
        courseDataModel.setPrograms(courseProgramService.getAllCourseProgramModelByCourseId(courseId));
        courseDataModel.setLikes(likeService.getAllLikeModelByCourseId(courseId));
        courseDataModel.setComments(commentService.getAllCommentModelByCourseId(courseId));
        return courseDataModel;
    }

    @Override
    public List<CourseDataModel> getAllCourseDataModelByCourseName(String courseName) {
        return courseRepository.findAllByCourseName(courseName.toLowerCase(Locale.ROOT))
                .stream()
                .map(i -> getCourseDataModelByCourseId(i.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDataModel> getAllCourseDataModelByCategoryName(String categoryName) {
        return courseRepository.findAllByCategoryName(categoryName.toLowerCase(Locale.ROOT))
                .stream()
                .map(i -> getCourseDataModelByCourseId(i.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDataModel> getAllCourseDataModelByCategoryId(Long id) {
        return courseRepository.findAllByCategory_Id(id)
                .stream()
                .map(i -> getCourseDataModelByCourseId(i.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDataModel> getAllCourseDataModelByUserId(Long userId) {
        return courseRepository.findAllByUser_Id(userId)
                .stream()
                .map(i -> getCourseDataModelByCourseId(i.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public CourseDataModel updateCourse(UpdateCourseModel updateCourseModel) {
        Long courseId = updateCourseModel.getId();

        Course dataCourse = getDataCourseByIdWithCheckAccess(courseId);

        validateEmailAndPhoneNumber(updateCourseModel);
        validateVariablesForNullOrIsEmptyUpdate(updateCourseModel);
        validateLengthVariables(updateCourseModel);

        setVariablesForUpdateCourse(dataCourse, updateCourseModel);
        courseRepository.save(dataCourse);
        return getCourseDataModelByCourseId(courseId);
    }

    @Override
    public CourseModel deleteCourseById(Long id) {
        Course deleteCourse = getDataCourseByIdWithCheckAccess(id);
        courseRepository.deleteById(id);
        return courseConverter.convertFromEntity(deleteCourse);
    }

    private Course initCourse(CreateCourseModel createCourseModel) {
        Course course = new Course();
        Category category = new Category();
        if (createCourseModel.getCategoryId() == null) {
            category.setId(8L);
        } else category.setId(createCourseModel.getCategoryId());

        course.setCategory(category);
        course.setCourseName(createCourseModel.getCourseName().toLowerCase(Locale.ROOT));
        course.setCourseShortInfo(createCourseModel.getCourseShortInfo());
        course.setCourseInfo(createCourseModel.getCourseInfo());
        course.setCourseInfoTitle(createCourseModel.getCourseInfoTitle());
        course.setCourseInfoUrl(createCourseModel.getCourseInfoUrl());
        course.setPhoneNumber(createCourseModel.getPhoneNumber());
        course.setEmail(createCourseModel.getEmail());
        course.setPrice(createCourseModel.getPrice());
        course.setUser(userService.getCurrentUser());
        return course;
    }

    private Course getDataCourseByIdWithCheckAccess(Long id) {
        if (id == null)
            throw new ApiFailException("Не указан ID курса");

        Course dataCourse = getById(id);

        if (dataCourse == null)
            throw new ApiFailException("Курс под ID " + id + " не найден");

        Long currentUserId = userService.getCurrentUser().getId();
        Long authorCourseId = dataCourse.getUser().getId();

        if (!currentUserId.equals(authorCourseId))
            throw new ApiFailException("Доступ ограничен");

        return dataCourse;
    }

    private void validateEmailAndPhoneNumber(BaseCourseModel baseCourseModel) {
        if (baseCourseModel.getEmail() != null)
            RegexUtil.validateEmail(baseCourseModel.getEmail());
        if (baseCourseModel.getPhoneNumber() != null) {
            RegexUtil.validatePhoneNumber(baseCourseModel.getPhoneNumber());
        }
    }

    private void validateLengthVariables(BaseCourseModel baseCourseModel) {
        if (baseCourseModel.getCourseShortInfo() != null && baseCourseModel.getCourseShortInfo().length() > 300)
            throw new ApiFailException("Длинна символов короткой информации о курсе ограниченно(300)");
        if (baseCourseModel.getCourseInfoTitle() != null && baseCourseModel.getCourseInfoTitle().length() > 100)
            throw new ApiFailException("Длинна символов заголовка информации ограниченно(100)");
        if (baseCourseModel.getCourseInfo() != null && baseCourseModel.getCourseInfo().length() > 1000)
            throw new ApiFailException("Длинна символов информации о курсе ограниченно(1000)");
    }

    private void setVariablesForUpdateCourse(Course course, UpdateCourseModel updateCourseModel) {
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

    public void validateVariablesForNullOrIsEmpty(CreateCourseModel createCourseModel) {
        if (createCourseModel.getCourseName() == null || createCourseModel.getCourseName().isEmpty())
            throw new ApiFailException("Название курса не заполнено");
        if (createCourseModel.getCourseShortInfo() == null || createCourseModel.getCourseShortInfo().isEmpty())
            throw new ApiFailException("Короткая информация о курсе не заполнено");
        if (createCourseModel.getPrice() == null)
            throw new ApiFailException("Цена курса не указана");
        else if (createCourseModel.getPrice().compareTo(BigDecimal.ZERO) < 0)
            throw new ApiFailException("Не правильный формат");
    }

    public void validateVariablesForNullOrIsEmptyUpdate(UpdateCourseModel updateCourseModel) {
        if (updateCourseModel.getCourseName() != null && updateCourseModel.getCourseName().isEmpty())
            throw new ApiFailException("Название курса не заполнено");
        if (updateCourseModel.getCourseShortInfo() != null && updateCourseModel.getCourseShortInfo().isEmpty())
            throw new ApiFailException("Короткая информация о курсе не заполнено");
        if (updateCourseModel.getPrice() != null && updateCourseModel.getPrice().compareTo(BigDecimal.ZERO) < 0)
            throw new ApiFailException("Не правильный формат");
    }
}