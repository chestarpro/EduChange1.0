package kg.itacademy.service.impl;

import kg.itacademy.converter.UserBalanceConverter;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserBalance;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.balance.UpdateUserBalanceModel;
import kg.itacademy.model.balance.UserBalanceModel;
import kg.itacademy.repository.UserBalanceRepository;
import kg.itacademy.service.UserBalanceService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBalanceServiceImpl implements UserBalanceService {
    @Autowired
    private UserService userService;
    private final UserBalanceRepository userBalanceRepository;
    private final UserBalanceConverter userBalanceConverter;

    @Override
    public UserBalance save(UserBalance userBalance) {
        return userBalanceRepository.save(userBalance);
    }

    @Override
    public UserBalance getUserBalanceByUserId(Long userId) {
        return userBalanceRepository.findByUser_Id(userId).orElse(null);
    }

    @Override
    public UserBalance getById(Long id) {
        return userBalanceRepository.findById(id).orElse(null);
    }

    @Override
    public UserBalanceModel getUserBalanceModelById(Long id) {
        return userBalanceConverter.convertFromEntity(getById(id));
    }

    @Override
    public UserBalanceModel getUserBalanceModelByUserId(Long userId) {
        UserBalance userBalance = getUserBalanceByUserId(userId);
        return userBalanceConverter.convertFromEntity(userBalance);
    }

    @Override
    public List<UserBalance> getAll() {
        return userBalanceRepository.findAll();
    }

    @Override
    public UserBalanceModel toUpBalance(UpdateUserBalanceModel updateUserBalanceModel) {
        String username = updateUserBalanceModel.getUsername();

        if (username == null || username.isEmpty())
            throw new ApiFailException("Username is not filled");

        BigDecimal balance = updateUserBalanceModel.getBalance();

        if (balance == null)
            throw new ApiFailException("User balance is not filled");

        if (balance.compareTo(BigDecimal.ZERO) <= 0)
            throw new ApiFailException("Amount must not be less than or equal to 0");

        User dataUser = userService.getByUsername(username);

        if (dataUser == null)
            throw new ApiFailException("User " + username + " not found");

        UserBalance dataUserBalance = getUserBalanceByUserId(dataUser.getId());
        dataUserBalance.setBalance(dataUserBalance.getBalance().add(balance));
        userBalanceRepository.save(dataUserBalance);
        return userBalanceConverter.convertFromEntity(dataUserBalance);
    }
}