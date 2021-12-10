package kg.itacademy.repository;

import kg.itacademy.entity.UserCourseMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCourseMappingRepository extends JpaRepository<UserCourseMapping, Long> {
    List<UserCourseMapping> findAllByUser_Id(Long id);
    Optional<UserCourseMapping> findByCourse_IdAndUser_Id(Long courseId, Long userId);
}
