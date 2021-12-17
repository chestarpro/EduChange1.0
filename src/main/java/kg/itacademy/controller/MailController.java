package kg.itacademy.controller;

import kg.itacademy.model.user.UserProfileDataModel;
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

    private final MailService MAIL_SERVICE;
    private final UserService USER_SERVICE;

    @GetMapping("/send-url-reset/{email}")
    public boolean send(@PathVariable String email) {
        return MAIL_SERVICE.send(email);
    }

    @PutMapping("/reset-password")
    private ResponseMessage<UserProfileDataModel> resetPassword(@RequestBody ResetPasswordModel resetPasswordModel) {
        return new ResponseMessage<UserProfileDataModel>()
                .prepareSuccessMessage(USER_SERVICE.resetPassword(resetPasswordModel));
    }
}