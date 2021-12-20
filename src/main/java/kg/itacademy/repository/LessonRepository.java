package kg.itacademy.repository;

import kg.itacademy.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query(value = "SELECT * FROM lessons l  WHERE l.course_id = :courseId ORDER BY id ASC LIMIT 3;", nativeQuery = true)
    List<Lesson> findFirstThreeLessonsByCourseId(@Param("courseId") Long courseId);

    List<Lesson> findAllByCourse_Id(Long courseId);

    @Query(value = "select count(*) from lessons l where l.course_id = :courseId", nativeQuery = true)
    Long getCountLessonByCourseId(@Param("courseId") Long courseId);
}