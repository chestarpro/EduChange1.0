package kg.itacademy.service.impl;

import kg.itacademy.converter.UserCourseMappingConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserBalance;
import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.course.CourseDataModel;
import kg.itacademy.model.UserCourseMappingModel;
import kg.itacademy.repository.UserCourseMappingRepository;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.UserBalanceService;
import kg.itacademy.service.UserCourseMappingService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCourseMappingServiceImpl implements UserCourseMappingService {
    @Autowired
    private CourseService COURSE_SERVICE;
    @Autowired
    private UserService USER_SERVICE;
    @Autowired
    private UserBalanceService USER_BALANCE_SERVICE;
    private final UserCourseMappingConverter MAPPING_CONVERTER;
    private final UserCourseMappingRepository USER_COURSE_MAPPING_REPOSITORY;

    @Override
    public UserCourseMapping save(UserCourseMapping userCourseMapping) {
        return USER_COURSE_MAPPING_REPOSITORY.save(userCourseMapping);
    }

    @Override
    public UserCourseMappingModel createByCourseId(Long courseId) {
        Course dataCourse = COURSE_SERVICE.getById(courseId);

        if (dataCourse == null)
            throw new ApiFailException("Course by id " + courseId + " not found");

        User user = USER_SERVICE.getCurrentUser();
        Long currentUserId = user.getId();
        Long authorCourseId = dataCourse.getUser().getId();

        UserCourseMapping dataUserCourseMapping = getByCourseIdAndUserId(courseId, currentUserId);

        if (dataUserCourseMapping != null)
            throw new ApiFailException("Bought already!");

        if (authorCourseId.equals(currentUserId))
            throw new ApiFailException("User " + user.getUsername() + " is the author of the course");

        transaction(dataCourse.getPrice(), currentUserId, authorCourseId);
        return MAPPING_CONVERTER.convertFromEntity(save(new UserCourseMapping(user, dataCourse)));
    }

    @Override
    public UserCourseMapping getById(Long id) {
        return USER_COURSE_MAPPING_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public UserCourseMappingModel getUserCourseMappingModelById(Long id) {
        return MAPPING_CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public UserCourseMapping getByCourseIdAndUserId(Long courseId, Long userId) {
        return USER_COURSE_MAPPING_REPOSITORY
                .findByCourse_IdAndUser_Id(courseId, userId)
                .orElse(null);
    }

    @Override
    public List<UserCourseMapping> getAll() {
        return USER_COURSE_MAPPING_REPOSITORY.findAll();
    }

    @Override
    public List<CourseDataModel> getAllPurchasedCourses(Long userId) {
        return USER_COURSE_MAPPING_REPOSITORY
                .findAllByUser_Id(userId)
                .stream()
                .map(UserCourseMapping::getCourse)
                .map(i -> COURSE_SERVICE.getCourseDataModelByCourseId(i.getId()))
                .collect(Collectors.toList());
    }

    private void transaction(BigDecimal coursePrice, Long currentUserId, Long authorCourseUserId) {
        UserBalance currentUserBalance = USER_BALANCE_SERVICE.getUserBalanceByUserId(currentUserId);

        if (currentUserBalance.getBalance().compareTo(coursePrice) < 0)
            throw new ApiFailException("Not enough balance");

        currentUserBalance.setBalance(currentUserBalance.getBalance().subtract(coursePrice));
        USER_BALANCE_SERVICE.save(currentUserBalance);

        UserBalance authorUserBalance = USER_BALANCE_SERVICE.getUserBalanceByUserId(authorCourseUserId);
        authorUserBalance.setBalance(authorUserBalance.getBalance().add(coursePrice));
        USER_BALANCE_SERVICE.save(authorUserBalance);
    }
}