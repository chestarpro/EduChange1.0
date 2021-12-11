package kg.itacademy.util;

import kg.itacademy.exception.ApiFailException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegexUtil {

    public void validatePhoneNumber(String phoneNumber) {
        String phoneNumberRegex = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
        Matcher matcher = Pattern.compile(phoneNumberRegex).matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new ApiFailException("Incorrect phone number format");
        }
    }

    public void validateEmail(String email) {
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Matcher matcher = Pattern.compile(emailRegex).matcher(email);
        if (!matcher.matches()) {
            throw new ApiFailException("Incorrect email format");
        }
    }

    public void validateUsername(String username) {
        String usernameRegex = "^[a-zA-Z0-9._-]{3,}$";
        Matcher matcher = Pattern.compile(usernameRegex).matcher(username);
        if (!matcher.matches()) {
            throw new ApiFailException("Incorrect username format");
        }
    }
}