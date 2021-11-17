package kg.itacademy.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "users_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserImage extends BaseEntity {
    @Column(name = "user_image_url", nullable = false)
    private String userImageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}