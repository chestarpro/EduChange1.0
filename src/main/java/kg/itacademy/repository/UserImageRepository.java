package kg.itacademy.repository;

import kg.itacademy.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<UserImage> findByUser_Id(Long id);
}