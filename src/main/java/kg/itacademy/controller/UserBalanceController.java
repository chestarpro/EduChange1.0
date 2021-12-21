package kg.itacademy.controller;

import kg.itacademy.util.ResponseMessage;
import kg.itacademy.model.balance.UpdateUserBalanceModel;
import kg.itacademy.model.balance.UserBalanceModel;
import kg.itacademy.service.UserBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
public class UserBalanceController {
    private final UserBalanceService userBalanceService;

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<UserBalanceModel> getById(@PathVariable Long id) {
        return new ResponseMessage<UserBalanceModel>()
                .prepareSuccessMessage(userBalanceService.getUserBalanceModelById(id));
    }

    @GetMapping("/get-by-user-id/{userId}")
    public ResponseMessage<UserBalanceModel> getByUserId(@PathVariable Long userId) {
        return new ResponseMessage<UserBalanceModel>()
                .prepareSuccessMessage(userBalanceService.getUserBalanceModelByUserId(userId));
    }

    @PutMapping("/update")
    public ResponseMessage<UserBalanceModel> update(@RequestBody UpdateUserBalanceModel updateUserBalanceModel) {
        return new ResponseMessage<UserBalanceModel>()
                .prepareSuccessMessage(userBalanceService.toUpBalance(updateUserBalanceModel));
    }
}