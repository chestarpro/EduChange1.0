package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseConverter;
import kg.itacademy.converter.UserCourseMappingConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.CourseModel;
import kg.itacademy.model.UserCourseMappingModel;
import kg.itacademy.repository.UserCourseMappingRepository;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.UserCourseMappingService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCourseMappingServiceImpl implements UserCourseMappingService {

    private final UserCourseMappingRepository userCourseMappingRepository;

    private final CourseService courseService;

    private final UserService userService;

    @Override
    public UserCourseMapping save(UserCourseMapping userCourseMapping) {
        Course course = courseService.getById(userCourseMapping.getCourse().getId());
        User user = userService.getCurrentUser();
        if (course.getUser().getId().equals(user.getId()))
            throw new ApiFailException("Пользователь " + user.getUsername() + " является автором курса");
        return userCourseMappingRepository.save(userCourseMapping);
    }

    @Override
    public UserCourseMappingModel createByIdCourse(Long id) {
        User user = userService.getCurrentUser();
        Course course = courseService.getById(id);

        return new UserCourseMappingConverter()
                .convertFromEntity(save(new UserCourseMapping(user, course)));
    }

    @Override
    public UserCourseMapping getById(Long id) {
        UserCourseMapping userCourseMapping = userCourseMappingRepository.findById(id).orElse(null);
        if (userCourseMapping == null)
            throw new ApiFailException("Не найдена связка по id: " + id);
        return userCourseMapping;
    }

    @Override
    public UserCourseMappingModel getUserCourseMappingModelById(Long id) {
        return new UserCourseMappingConverter()
                .convertFromEntity(getById(id));
    }

    @Override
    public List<UserCourseMapping> getAll() {
        return userCourseMappingRepository.findAll();
    }

    @Override
    public List<UserCourseMappingModel> getAllUserCourseMappingModel() {
        List<UserCourseMappingModel> mappingModels = new ArrayList<>();
        for (UserCourseMapping mapping : getAll())
            mappingModels.add(new UserCourseMappingConverter().convertFromEntity(mapping));
        return mappingModels;
    }

    @Override
    public UserCourseMapping update(UserCourseMapping userCourseMapping) {
        return null;
    }

    @Override
    public List<CourseModel> getAllPurchasedCourses() {
        List<Course> purchasedCourses = userCourseMappingRepository
                .findAllByUser_Id(
                        userService
                                .getCurrentUser()
                                .getId())
                .stream()
                .map(UserCourseMapping::getCourse)
                .collect(Collectors.toList()
                );

        List<CourseModel> courseModels = new ArrayList<>();
        for (Course course : purchasedCourses)
            courseModels.add(new CourseConverter().convertFromEntity(course));
        return courseModels;
    }
}