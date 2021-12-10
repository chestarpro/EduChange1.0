package kg.itacademy.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserLogModel {
    private Long id;
    private Long userId;
    private Boolean isSuccess;
    private LocalDateTime createDate;
}
