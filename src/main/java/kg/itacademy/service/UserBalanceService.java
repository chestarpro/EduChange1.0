package kg.itacademy.service;

import kg.itacademy.entity.UserBalance;
import kg.itacademy.model.balance.UpdateUserBalanceModel;
import kg.itacademy.model.balance.UserBalanceModel;

import java.util.List;

public interface UserBalanceService extends BaseService<UserBalance> {
    UserBalanceModel toUpBalance(UpdateUserBalanceModel updateUserBalanceModel);

    UserBalance getUserBalanceByUserId(Long userId);

    UserBalanceModel getUserBalanceModelByUserId(Long userId);

    UserBalanceModel getUserBalanceModelById(Long id);
}