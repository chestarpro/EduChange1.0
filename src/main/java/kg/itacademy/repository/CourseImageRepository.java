package kg.itacademy.repository;

import kg.itacademy.entity.CourseImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseImageRepository extends JpaRepository<CourseImage, Long> {
    Optional<CourseImage> findByCourse_Id(Long courseId);
}