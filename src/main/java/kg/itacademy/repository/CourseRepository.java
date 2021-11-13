package kg.itacademy.repository;

import kg.itacademy.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "FROM Course c WHERE c.courseName LIKE ':courseName%'")
    List<Course> findAllByCourseName(@Param("courseName") String courseName);

    List<Course> findAllByCategory_Id(Long id);

    @Query(value = "FROM Course c WHERE c.category.categoryName LIKE ':categoryName%'")
    List<Course> findAllByCategoryName(@Param("categoryName") String categoryName);

    List<Course> findAllByUser_Id(Long id);
}
