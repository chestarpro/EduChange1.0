package kg.itacademy.util;

import kg.itacademy.exception.ApiFailException;
import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class RegexUtil {

    public static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber != null) {
            String phoneNumberRegex = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
            Matcher matcher = Pattern.compile(phoneNumberRegex).matcher(phoneNumber);
            if (!matcher.matches()) {
                throw new ApiFailException("Incorrect phone number format");
            }
        }
    }

    public static void validateEmail(String email) {
        if (email != null) {
            String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
            Matcher matcher = Pattern.compile(emailRegex).matcher(email);
            if (!matcher.matches()) {
                throw new ApiFailException("Incorrect email format");
            }
        }
    }

    public static void validateUsername(String username) {
        if (username != null) {
            String usernameRegex = "^[a-zA-Z0-9._-]{3,}$";
            Matcher matcher = Pattern.compile(usernameRegex).matcher(username);
            if (!matcher.matches()) {
                throw new ApiFailException("Incorrect username format");
            }
        }
    }
}