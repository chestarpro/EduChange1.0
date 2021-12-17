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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBalanceImpl implements UserBalanceService {
    @Autowired
    private final UserService USER_SERVICE;
    private final UserBalanceRepository USER_BALANCE_REPOSITORY;
    private final UserBalanceConverter USER_BALANCE_CONVERTER;

    @Override
    public UserBalance save(UserBalance userBalance) {
        return USER_BALANCE_REPOSITORY.save(userBalance);
    }

    @Override
    public UserBalance getUserBalanceByUserId(Long userId) {
        return USER_BALANCE_REPOSITORY.findByUser_Id(userId);
    }

    @Override
    public UserBalance getById(Long id) {
        return USER_BALANCE_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public UserBalanceModel getUserBalanceModelById(Long id) {
        return USER_BALANCE_CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public UserBalanceModel getUserBalanceModelByUserId(Long userId) {
        UserBalance userBalance = USER_BALANCE_REPOSITORY.findByUser_Id(userId);
        return USER_BALANCE_CONVERTER.convertFromEntity(userBalance);
    }

    @Override
    public List<UserBalance> getAll() {
        return USER_BALANCE_REPOSITORY.findAll();
    }

    @Override
    public List<UserBalanceModel> getAllUserBalanceModel() {
        return getAll().stream()
                .map(USER_BALANCE_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UserBalanceModel toUpBalance(UpdateUserBalanceModel updateUserBalanceModel) {
        String username = updateUserBalanceModel.getUsername();
        BigDecimal balance = updateUserBalanceModel.getBalance();

        if (username == null || username.isEmpty())
            throw new ApiFailException("Username is not filled");

        if (balance == null)
            throw new ApiFailException("User balance is not filled");

        if (balance.compareTo(BigDecimal.ZERO) <= 0)
            throw new ApiFailException("Amount must not be less than or equal to 0");

        User dataUser = USER_SERVICE.getByUsername(updateUserBalanceModel.getUsername());

        if (dataUser == null)
            throw new ApiFailException("User " + updateUserBalanceModel.getUsername() + " not found");

        UserBalance dataUserBalance = USER_BALANCE_REPOSITORY.findByUser_Id(dataUser.getId());
        dataUserBalance.setBalance(dataUserBalance.getBalance().add(balance));
        USER_BALANCE_REPOSITORY.save(dataUserBalance);
        return USER_BALANCE_CONVERTER.convertFromEntity(dataUserBalance);
    }
}