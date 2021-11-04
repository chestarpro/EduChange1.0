package kg.itacademy.repository;

import kg.itacademy.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByCourseName(String courseName);

    List<Course> findAllByCategory_Id(Long id);

    List<Course> findAllByCategory_CategoryName(String categoryName);

    List<Course> findAllByUser_Id(Long id);
}
