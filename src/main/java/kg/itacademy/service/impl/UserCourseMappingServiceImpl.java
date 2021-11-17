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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
            throw new ApiFailException("User " + user.getUsername() + " is the author of the course");
        return userCourseMappingRepository.save(userCourseMapping);
    }

    @Override
    public UserCourseMappingModel createByCourseId(Long courseId) {
        User user = userService.getCurrentUser();
        Course course = courseService.getById(courseId);

        return new UserCourseMappingConverter()
                .convertFromEntity(save(new UserCourseMapping(user, course)));
    }

    @Override
    public UserCourseMapping getById(Long id) {
        UserCourseMapping userCourseMapping = userCourseMappingRepository.findById(id).orElse(null);
        if (userCourseMapping == null)
            throw new ApiFailException("The purchase by ID(" + id + ") not found");
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
        UserCourseMappingConverter converter = new UserCourseMappingConverter();
        return getAll().stream()
                .map(converter::convertFromEntity).collect(Collectors.toList());
    }

    @Override
    public List<CourseModel> getAllPurchasedCourses() {
        List<Course> purchasedCourses = userCourseMappingRepository
                .findAllByUser_Id(userService.getCurrentUser().getId())
                .stream().map(UserCourseMapping::getCourse)
                .collect(Collectors.toList());

        if (!purchasedCourses.isEmpty()) {
            CourseConverter converter = new CourseConverter();
            return purchasedCourses.stream().map(converter::convertFromEntity).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public UserCourseMapping update(UserCourseMapping userCourseMapping) {
        return null;
    }

    @Override
    public UserCourseMappingModel deleteMapping(Long id) {
        UserCourseMapping mapping = getById(id);
        userCourseMappingRepository.delete(mapping);
        return new UserCourseMappingConverter().convertFromEntity(mapping);
    }
}