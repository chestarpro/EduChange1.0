package kg.itacademy.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserModel implements BaseUser {
    private String fullName;
    private LocalDate birthDay;
    private String username;
    private String email;
    private String password;
    private Long isActive;
}