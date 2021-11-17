package kg.itacademy.repository;

import kg.itacademy.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserLogRepository extends JpaRepository<UserLog, Long> {

    @Query(nativeQuery = true,
            value = "select\n" +
                    "\tcase\n" +
                    "\t\twhen count(*) = 3 then true\n" +
                    "\t\telse false\n" +
                    "\tend\n" +
                    "from\n" +
                    "\t(\n" +
                    "\tselect\n" +
                    "\t\t*\n" +
                    "\tfrom\n" +
                    "\t\tusers_authorization_logs\n" +
                    "\twhere\n" +
                    "\t\tuser_id = :userId\n" +
                    "\torder by\n" +
                    "\t\tid desc\n" +
                    "\tlimit 3) as logs\n" +
                    "where\n" +
                    "\tlogs.is_success = false and logs.create_date between now() - interval '1 HOUR' and now();"
    )
    Boolean hasThreeFailsInARowByUserId(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = "select * from users_authorization_logs where user_id = :userId order by id desc limit 1")
    Optional<UserLog> findLastLogByUserId(@Param("userId") Long userId);

    List<UserLog> findAllByUser_Id(Long id);
}