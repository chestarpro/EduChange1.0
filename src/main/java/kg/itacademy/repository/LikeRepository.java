package kg.itacademy.repository;

import kg.itacademy.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllByCourse_Id(Long id);
    Optional<Like> findByCourse_IdAndUser_Id(Long curseId, Long userId);
}
