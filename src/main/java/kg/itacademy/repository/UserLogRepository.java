package kg.itacademy.repository;

import kg.itacademy.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserLogRepository extends JpaRepository<UserLog, Long> {
    List<UserLog> findAllByUser_Id(Long id);

    @Query(nativeQuery = true,
            value = "SELECT CASE WHEN COUNT(*) = 3 THEN true ELSE false END " +
                    "FROM(SELECT * FROM users_authorization_logs WHERE user_id = :userId " +
                    "ORDER BY id DESC LIMIT 3) AS logs WHERE logs.is_success = false AND " +
                    "logs.create_date BETWEEN now() - INTERVAL '1 HOUR' AND now();"
    )
    Boolean hasThreeFailsInARowByUserId(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = "SELECT * FROM users_authorization_logs WHERE user_id = :userId ORDER BY id DESC LIMIT 1")
    Optional<UserLog> findLastLogByUserId(@Param("userId") Long userId);
}