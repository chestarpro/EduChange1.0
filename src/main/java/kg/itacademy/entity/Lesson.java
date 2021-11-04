package kg.itacademy.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson extends BaseEntity {
    @Column(name = "lesson_info")
    private String lessonInfo;

    @Column(name = "lesson_url")
    private String lessonUrl;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
