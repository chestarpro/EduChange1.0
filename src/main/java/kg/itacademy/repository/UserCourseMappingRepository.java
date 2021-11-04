package kg.itacademy.repository;

import kg.itacademy.entity.Course;
import kg.itacademy.entity.UserCourseMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCourseMappingRepository extends JpaRepository<UserCourseMapping, Long> {
    List<Course> findAllCourseByUser_Id(Long id);
}
