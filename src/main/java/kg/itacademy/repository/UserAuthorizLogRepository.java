package kg.itacademy.repository;

import kg.itacademy.entity.UserAuthorizLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthorizLogRepository extends JpaRepository<UserAuthorizLog, Long> {
}
