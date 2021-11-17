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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
        UserBalance balance = userBalanceRepository.findById(id).orElse(null);
        if (balance == null)
            throw new ApiFailException("User balance by ID(" + id + ") not found");
        return balance;
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
        UserBalanceConverter converter = new UserBalanceConverter();
        return getAll().stream()
                .map(converter::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UserBalance update(UserBalance userBalance) {
        if (userBalance.getBalance().compareTo(BigDecimal.ZERO) < 0)
            throw new ApiFailException("Баланс (" + userBalance.getBalance() + ") не должен быть меньше 0");

        return userBalanceRepository.save(userBalance);
    }

    @Override
    public UserBalanceModel updateByUpdateUserBalanceModel(UpdateUserBalanceModel updateUserBalanceModel) {
        User user = userService.getByUsername(updateUserBalanceModel.getUsername());
        if (user == null)
            throw new ApiFailException("User (" + updateUserBalanceModel.getUsername() + ") not found");
        UserBalance userBalance = userBalanceRepository.findByUser_Id(user.getId());
        userBalance.setBalance(updateUserBalanceModel.getBalance());
        return new UserBalanceConverter().convertFromEntity(update(userBalance));
    }
}
