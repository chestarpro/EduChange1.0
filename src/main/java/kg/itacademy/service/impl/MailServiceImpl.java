package kg.itacademy.service.impl;

import kg.itacademy.service.MailService;
import kg.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Base64;
import java.util.Objects;

@Service
public class MailServiceImpl implements MailService {
    private final String RESET_URL = "http://localhost:8080/api/mail/reset-password";
    private final String INFO = "Carefully! If you haven't tried to reset your password, ignore this message!\n" +
                                "If this is you, then follow the link and enter the password reset key. ";

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private Environment environment;

    @Override
    public boolean send(String email) {
       String encodeEmail = new String(Base64.getEncoder().encode(email.getBytes()));
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject(INFO + encodeEmail, "UTF-8");
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
            mimeMessageHelper.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(RESET_URL, true);
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}