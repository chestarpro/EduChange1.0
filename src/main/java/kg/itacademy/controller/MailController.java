package kg.itacademy.controller;

import kg.itacademy.model.user.BaseUserModel;
import kg.itacademy.model.userImage.ResetPasswordModel;
import kg.itacademy.service.MailService;
import kg.itacademy.service.UserService;
import kg.itacademy.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final UserService userService;

    @GetMapping("/send-message-reset-password/{email}")
    public boolean send(@PathVariable String email) {
        return mailService.send(email);
    }

    @PutMapping("/reset-password")
    private ResponseMessage<BaseUserModel> resetPassword(@RequestBody ResetPasswordModel resetPasswordModel) {
        return new ResponseMessage<BaseUserModel>()
                .prepareSuccessMessage(userService.resetPassword(resetPasswordModel));
    }
}