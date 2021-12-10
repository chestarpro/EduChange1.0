package kg.itacademy.service;

import kg.itacademy.entity.UserBalance;
import kg.itacademy.model.user.UpdateUserBalanceModel;
import kg.itacademy.model.user.UserBalanceModel;

import java.util.List;

public interface UserBalanceService extends BaseService<UserBalance> {
    UserBalanceModel getUserBalanceModelByUserId(Long userId);
    List<UserBalanceModel> getAllUserBalanceModel();
    UserBalanceModel getUserBalanceModelById(Long id);

    UserBalanceModel updateByUpdateUserBalanceModel(UpdateUserBalanceModel updateUserBalanceModel);
}
