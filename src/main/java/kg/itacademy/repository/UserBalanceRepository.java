package kg.itacademy.repository;

import kg.itacademy.entity.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {
    UserBalance findByUser_Id(Long userId);
}
