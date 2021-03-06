package kg.itacademy.service.impl;

import kg.itacademy.converter.UserCourseMappingConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserBalance;
import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.course.CourseDataModel;
import kg.itacademy.model.UserCourseMappingModel;
import kg.itacademy.model.user.BaseUserModel;
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
    private CourseService courseService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserBalanceService userBalanceService;
    private final UserCourseMappingConverter userCourseMappingConverter;
    private final UserCourseMappingRepository userCourseMappingRepository;

    @Override
    public UserCourseMapping save(UserCourseMapping userCourseMapping) {
        return userCourseMappingRepository.save(userCourseMapping);
    }

    @Override
    public UserCourseMappingModel createByCourseId(Long courseId) {
        Course dataCourse = getCourseWithCheckForNull(courseId);

        User user = userService.getCurrentUser();
        Long currentUserId = user.getId();
        Long authorCourseId = dataCourse.getUser().getId();

        validatePurchaseWasMade(courseId, currentUserId, authorCourseId);
        transaction(dataCourse.getPrice(), currentUserId, authorCourseId);
        return userCourseMappingConverter.convertFromEntity(save(new UserCourseMapping(user, dataCourse)));
    }

    @Override
    public UserCourseMapping getById(Long id) {
        return userCourseMappingRepository.findById(id).orElse(null);
    }

    @Override
    public UserCourseMappingModel getUserCourseMappingModelById(Long id) {
        return userCourseMappingConverter.convertFromEntity(getById(id));
    }

    @Override
    public List<BaseUserModel> getAllCustomersByCourseId(Long courseId) {
        return userCourseMappingRepository
                .findAllByCourse_Id(courseId)
                .stream()
                .map(UserCourseMapping::getUser)
                .map(i -> userService.getUserModelById(i.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public UserCourseMapping getByCourseIdAndUserId(Long courseId, Long userId) {
        return userCourseMappingRepository
                .findByCourse_IdAndUser_Id(courseId, userId)
                .orElse(null);
    }

    @Override
    public List<UserCourseMapping> getAll() {
        return userCourseMappingRepository.findAll();
    }

    @Override
    public List<CourseDataModel> getAllPurchasedCourses(Long userId) {
        return userCourseMappingRepository
                .findAllByUser_Id(userId)
                .stream()
                .map(UserCourseMapping::getCourse)
                .map(i -> courseService.getCourseDataModelByCourseId(i.getId()))
                .collect(Collectors.toList());
    }

    private void transaction(BigDecimal coursePrice, Long currentUserId, Long authorCourseUserId) {
        UserBalance currentUserBalance = userBalanceService.getUserBalanceByUserId(currentUserId);

        if (currentUserBalance.getBalance().compareTo(coursePrice) < 0)
            throw new ApiFailException("???????????????????????? ??????????????");

        currentUserBalance.setBalance(currentUserBalance.getBalance().subtract(coursePrice));
        userBalanceService.save(currentUserBalance);

        UserBalance authorUserBalance = userBalanceService.getUserBalanceByUserId(authorCourseUserId);
        authorUserBalance.setBalance(authorUserBalance.getBalance().add(coursePrice));
        userBalanceService.save(authorUserBalance);
    }

    private void validatePurchaseWasMade(Long courseId,  Long currentUserId, Long authorCourseId) {
        UserCourseMapping dataUserCourseMapping = getByCourseIdAndUserId(courseId, currentUserId);
        if (dataUserCourseMapping != null)
            throw new ApiFailException("???????? ?????? ????????????????????");
        if (authorCourseId.equals(currentUserId))
            throw new ApiFailException("?????????? ???? ?????????? ???????????? ???????? ??????????");
    }

    private Course getCourseWithCheckForNull(Long courseId) {
        Course dataCourse = courseService.getById(courseId);
        if (dataCourse == null)
            throw new ApiFailException("???????? ?????? ID " + courseId + " ???? ????????????");

        return dataCourse;
    }
}