package kg.itacademy.entity;

import lombok.*;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "users_balances")
@Check(constraints = "balance >= 0")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBalance extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
}
