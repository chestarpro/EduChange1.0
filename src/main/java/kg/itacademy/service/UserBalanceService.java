package kg.itacademy.service;

import kg.itacademy.entity.UserBalance;
import kg.itacademy.model.balance.UpdateUserBalanceModel;
import kg.itacademy.model.balance.UserBalanceModel;

import java.util.List;

public interface UserBalanceService extends BaseService<UserBalance> {
    UserBalanceModel getUserBalanceModelByUserId(Long userId);
    UserBalance getUserBalanceByUserId(Long userId);
    List<UserBalanceModel> getAllUserBalanceModel();
    UserBalanceModel getUserBalanceModelById(Long id);

    UserBalanceModel updateByUpdateUserBalanceModel(UpdateUserBalanceModel updateUserBalanceModel);
}
