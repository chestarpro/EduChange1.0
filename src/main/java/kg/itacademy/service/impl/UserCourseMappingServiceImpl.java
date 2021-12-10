package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseConverter;
import kg.itacademy.converter.UserBalanceConverter;
import kg.itacademy.converter.UserCourseMappingConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserBalance;
import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.CourseModel;
import kg.itacademy.model.UserBalanceModel;
import kg.itacademy.model.UserCourseMappingModel;
import kg.itacademy.repository.UserCourseMappingRepository;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.UserBalanceService;
import kg.itacademy.service.UserCourseMappingService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCourseMappingServiceImpl implements UserCourseMappingService {

    private final UserCourseMappingRepository userCourseMappingRepository;

    private final CourseService courseService;

    private final UserService userService;

    private final UserBalanceService userBalanceService;

    private final UserCourseMappingConverter CONVERTER = new UserCourseMappingConverter();

    private final UserBalanceConverter converterBalance = new UserBalanceConverter();

    private final CourseConverter COURSE_CONVERTER = new CourseConverter();

    @Override
    public UserCourseMapping save(UserCourseMapping userCourseMapping) {
        Long userId = userCourseMapping.getUser().getId();
        Long courseId = userCourseMapping.getCourse().getId();

        UserCourseMapping dataBaseMapping = userCourseMappingRepository
                .findByCourse_IdAndUser_Id(courseId, userId)
                .orElse(null);

        if (dataBaseMapping != null) {
            throw new ApiFailException("Bought already!");
        }

        Course course = courseService.getById(courseId);
        User user = userService.getCurrentUser();
        if (course.getUser().getId().equals(user.getId()))
            throw new ApiFailException("User " + user.getUsername() + " is the author of the course");

        UserBalanceModel userBalanceModel = userBalanceService.getUserBalanceModelByUserId(user.getId());
        if (userBalanceModel.getUserBalance().subtract(course.getPrice()).doubleValue() < 0) {
            throw new ApiFailException("Not enough balance");
        } else {
            UserBalance userBalance = converterBalance.convertFromModel(userBalanceModel);
            userBalance.setBalance(userBalance.getBalance().subtract(course.getPrice()));
            userBalanceService.save(userBalance);
        }

        return userCourseMappingRepository.save(userCourseMapping);
    }

    @Override
    public UserCourseMappingModel createByCourseId(Long courseId) {
        User user = userService.getCurrentUser();
        Course course = courseService.getById(courseId);

        return CONVERTER.convertFromEntity(save(new UserCourseMapping(user, course)));
    }

    @Override
    public UserCourseMapping getById(Long id) {
        return userCourseMappingRepository.findById(id).orElse(null);
    }

    @Override
    public UserCourseMappingModel getUserCourseMappingModelById(Long id) {
        return CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public List<UserCourseMapping> getAll() {
        return userCourseMappingRepository.findAll();
    }

    @Override
    public List<UserCourseMappingModel> getAllUserCourseMappingModel() {
        return getAll().stream()
                .map(CONVERTER::convertFromEntity).collect(Collectors.toList());
    }

    @Override
    public List<CourseModel> getAllPurchasedCourses(Long userId) {
        List<Course> purchasedCourses = userCourseMappingRepository
                .findAllByUser_Id(userId)
                .stream().map(UserCourseMapping::getCourse)
                .collect(Collectors.toList());

        return purchasedCourses.stream()
                .map(COURSE_CONVERTER::convertFromEntity).collect(Collectors.toList());
    }

    @Override
    public UserCourseMappingModel deleteMapping(Long id) {
        UserCourseMapping mapping = getById(id);
        userCourseMappingRepository.delete(mapping);
        return CONVERTER.convertFromEntity(mapping);
    }
}