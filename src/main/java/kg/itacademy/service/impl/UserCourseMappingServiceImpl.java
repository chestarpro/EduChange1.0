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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCourseMappingServiceImpl implements UserCourseMappingService {
    private final UserCourseMappingRepository USER_COURSE_MAPPING_REPOSITORY;
    private final CourseService COURSE_SERVICE;
    private final UserService USER_SERVICE;
    private final UserBalanceService USER_BALANCE_SERVICE;
    private final UserCourseMappingConverter MAPPING_CONVERTER;

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
        Long userId = user.getId();

        UserCourseMapping dataUserCourseMapping = USER_COURSE_MAPPING_REPOSITORY
                .findByCourse_IdAndUser_Id(courseId, userId)
                .orElse(null);

        if (dataUserCourseMapping != null) throw new ApiFailException("Bought already!");

        if (dataCourse.getUser().getId().equals(userId))
            throw new ApiFailException("User " + user.getUsername() + " is the author of the course");

        UserBalance userBalance = USER_BALANCE_SERVICE.getUserBalanceByUserId(userId);

        if (userBalance.getBalance().compareTo(dataCourse.getPrice()) < 0)
            throw new ApiFailException("Not enough balance");

        userBalance.setBalance(userBalance.getBalance().subtract(dataCourse.getPrice()));
        USER_BALANCE_SERVICE.save(userBalance);

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

    @Override
    public UserCourseMappingModel deleteMapping(Long id) {
        UserCourseMapping mapping = getById(id);
        USER_COURSE_MAPPING_REPOSITORY.delete(mapping);
        return MAPPING_CONVERTER.convertFromEntity(mapping);
    }
}