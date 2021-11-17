package kg.itacademy.service.impl;

import kg.itacademy.converter.UserBalanceConverter;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserBalance;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.UpdateUserBalanceModel;
import kg.itacademy.model.UserBalanceModel;
import kg.itacademy.repository.UserBalanceRepository;
import kg.itacademy.service.UserBalanceService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBalanceImpl implements UserBalanceService {

    private final UserBalanceRepository userBalanceRepository;

    @Autowired
    private UserService userService;

    @Override
    public UserBalance save(UserBalance userBalance) {
        return userBalanceRepository.save(userBalance);
    }

    @Override
    public UserBalance getById(Long id) {
        return userBalanceRepository.findById(id).orElse(null);
    }

    @Override
    public UserBalanceModel getUserBalanceModelById(Long id) {
        return new UserBalanceConverter().convertFromEntity(getById(id));
    }

    @Override
    public UserBalanceModel getUserBalanceModelByUserId(Long userId) {
        UserBalance userBalance = userBalanceRepository.findByUser_Id(userId);
        return new UserBalanceConverter()
                .convertFromEntity(userBalance);
    }

    @Override
    public List<UserBalance> getAll() {
        return userBalanceRepository.findAll();
    }

    @Override
    public List<UserBalanceModel> getAllUserBalanceModel() {
        List<UserBalanceModel> balanceModels = new ArrayList<>();
        for (UserBalance userBalance : getAll())
            balanceModels.add(new UserBalanceConverter().convertFromEntity(userBalance));
        return balanceModels;
    }

    @Override
    public UserBalance update(UserBalance userBalance) {
        if (userBalance.getUser() == null)
            throw new ApiFailException("Поле баланс null");

        if (userBalance.getBalance().compareTo(BigDecimal.ZERO) < 0)
            throw new ApiFailException("Баланс (" + userBalance.getBalance() + ") не должен быть меньше 0");

        return userBalanceRepository.save(userBalance);
    }

    @Override
    public UserBalanceModel updateByUpdateUserBalanceModel(UpdateUserBalanceModel updateUserBalanceModel) {
        User user = userService.getByUsername(updateUserBalanceModel.getUsername());
        if (user == null)
            throw new ApiFailException("Пользователь (" + updateUserBalanceModel.getUsername() + ") не найден");
        UserBalance userBalance = userBalanceRepository.findByUser_Id(user.getId());
        userBalance.setBalance(updateUserBalanceModel.getBalance());
        return new UserBalanceConverter().convertFromEntity(update(userBalance));
    }
}
