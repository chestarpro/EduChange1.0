package kg.itacademy.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "courses_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseImage extends BaseEntity {
    @Column(name = "course_image_url", nullable = false)
    private String courseImageUrl;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}