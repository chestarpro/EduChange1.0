package kg.itacademy.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "users_avatars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserImage extends BaseEntity {
    @Column(name = "user_image_url")
    private String userImageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}