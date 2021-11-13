package kg.itacademy.service.impl;

import kg.itacademy.entity.UserBalance;
import kg.itacademy.repository.UserBalanceRepository;
import kg.itacademy.service.UserBalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBalanceImpl implements UserBalanceService {

    private final UserBalanceRepository userBalanceRepository;

    @Override
    public UserBalance save(UserBalance userBalance) {
        return userBalanceRepository.save(userBalance);
    }

    @Override
    public UserBalance getById(Long id) {
        return userBalanceRepository.getById(id);
    }

    @Override
    public List<UserBalance> getAll() {
        return userBalanceRepository.findAll();
    }

    @Override
    public UserBalance update(UserBalance userBalance) {
        if (userBalance.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Не корректный баланс");
        }
        return userBalanceRepository.save(userBalance);
    }
}
