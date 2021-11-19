package kg.itacademy.service;

import kg.itacademy.entity.UserBalance;
import kg.itacademy.model.UpdateUserBalanceModel;
import kg.itacademy.model.UserBalanceModel;

import java.util.List;

public interface UserBalanceService extends BaseService<UserBalance> {
    UserBalanceModel getUserBalanceModelByUserId(Long userId);
    List<UserBalanceModel> getAllUserBalanceModel();
    UserBalanceModel getUserBalanceModelById(Long id);

    UserBalance update(UserBalance userBalance);

    UserBalanceModel updateByUpdateUserBalanceModel(UpdateUserBalanceModel updateUserBalanceModel);
}
