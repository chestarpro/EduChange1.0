package kg.itacademy.service.impl;

import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.repository.CourseRepository;
import kg.itacademy.repository.UserCourseMappingRepository;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.UserCourseMappingService;
import kg.itacademy.service.UserService;

import java.util.List;

public class UserCourseMappingServiceImpl implements UserCourseMappingService {


    private final UserCourseMappingRepository userCourseMappingRepository;

    private final CourseRepository courseRepository;

    private final UserService userService;

    private final CourseService courseService;

    public UserCourseMappingServiceImpl(UserCourseMappingRepository userCourseMappingRepository,
                                        CourseRepository courseRepository,
                                        UserService userService, CourseService courseService) {

        this.userCourseMappingRepository = userCourseMappingRepository;
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.courseService = courseService;
    }

    @Override
    public UserCourseMapping create(UserCourseMapping userCourseMapping) {
        return userCourseMappingRepository.save(userCourseMapping);
    }

    @Override
    public UserCourseMapping getById(Long id) {
        return null;
    }

    @Override
    public List<UserCourseMapping> getAll() {
        return userCourseMappingRepository.findAll();
    }

    @Override
    public UserCourseMapping update(UserCourseMapping userCourseMapping) {
        return null;
    }

    @Override
    public List<Course> findAllMyPurchasedCourses() {
        return userCourseMappingRepository
                .findAllCourseByUser_Id(userService.getCurrentUser().getId());
    }

    @Override
    public UserCourseMapping createByIdCourse(Long id) {
        User user = userService.getCurrentUser();
        Course course = courseRepository.findById(id).orElse(null);
        return create(new UserCourseMapping(user, course));
    }
}
