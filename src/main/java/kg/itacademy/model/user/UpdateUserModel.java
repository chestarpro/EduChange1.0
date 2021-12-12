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
public class UpdateUserModel implements BaseUserModel {
    private Long id;
    private String fullName;
    private LocalDate birthDay;
    private String username;
    private String email;
    private String password;
}