package kg.itacademy.repository;

import kg.itacademy.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    UserImage findByUser_Id(Long id);
}