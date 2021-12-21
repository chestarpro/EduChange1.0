package kg.itacademy.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson extends BaseEntity {
    @Column(name = "lesson_info", nullable = false, length = 1000)
    private String lessonInfo;

    @Column(name = "lesson_url")
    private String lessonUrl;

    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}