package kg.itacademy.service.impl;

import kg.itacademy.entity.Course;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserCourseMapping;
import kg.itacademy.repository.CourseRepository;
import kg.itacademy.repository.UserCourseMappingRepository;
import kg.itacademy.service.UserCourseMappingService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserCourseMappingServiceImpl implements UserCourseMappingService {


    private final UserCourseMappingRepository userCourseMappingRepository;

    private final CourseRepository courseRepository;

    private final UserService userService;

    @Override
    public UserCourseMapping create(UserCourseMapping userCourseMapping) {
        return userCourseMappingRepository.save(userCourseMapping);
    }

    @Override
    public UserCourseMapping getById(Long id) {
        return userCourseMappingRepository.getById(id);
    }

    @Override
    public List<UserCourseMapping> getAll() {
        return userCourseMappingRepository.findAll();
    }

    @Override
    public UserCourseMapping update(UserCourseMapping userCourseMapping) {

        return userCourseMappingRepository.save(userCourseMapping);
    }

    @Override
    public List<Course> findAllPurchasedCourses() {
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
