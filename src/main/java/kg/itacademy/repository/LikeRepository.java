package kg.itacademy.repository;

import kg.itacademy.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllByCourse_Id(Long id);
}
