package kg.itacademy.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "users_authorization_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthorizLog extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_success", nullable = false)
    private Boolean isSuccess;
}