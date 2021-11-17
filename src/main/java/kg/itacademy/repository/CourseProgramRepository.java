package kg.itacademy.repository;

import kg.itacademy.entity.CourseProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseProgramRepository extends JpaRepository<CourseProgram, Long> {
    List<CourseProgram> findAllByCourse_Id(Long courseId);
}
