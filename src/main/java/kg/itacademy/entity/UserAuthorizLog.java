package kg.itacademy.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users_authorization_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthorizLog extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}