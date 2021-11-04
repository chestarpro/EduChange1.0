package kg.itacademy.repository;

import kg.itacademy.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCourse_Id(Long id);
}
