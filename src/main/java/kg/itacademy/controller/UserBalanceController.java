package kg.itacademy.controller;

import kg.itacademy.model.ResponseMessage;
import kg.itacademy.model.UpdateUserBalanceModel;
import kg.itacademy.model.UserBalanceModel;
import kg.itacademy.service.UserBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balance")
public class UserBalanceController {

    @Autowired
    private UserBalanceService userBalanceService;

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
                .prepareSuccessMessage(userBalanceService.updateByUpdateUserBalanceModel(updateUserBalanceModel));
    }
}