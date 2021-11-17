package kg.itacademy.repository;

import kg.itacademy.entity.CourseImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseImageRepository extends JpaRepository<CourseImage, Long> {
    CourseImage findByCourse_Id(Long courseId);
}