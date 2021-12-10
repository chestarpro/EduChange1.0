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

    private final UserBalanceConverter CONVERTER = new UserBalanceConverter();

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
        return CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public UserBalanceModel getUserBalanceModelByUserId(Long userId) {
        UserBalance userBalance = userBalanceRepository.findByUser_Id(userId);
        return CONVERTER.convertFromEntity(userBalance);
    }

    @Override
    public List<UserBalance> getAll() {
        return userBalanceRepository.findAll();
    }

    @Override
    public List<UserBalanceModel> getAllUserBalanceModel() {
        return getAll().stream()
                .map(CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

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
        return CONVERTER.convertFromEntity(update(userBalance));
    }
}
