package kg.itacademy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CourseModel {
    private Long id;
    private Long categoryId;
    private String courseName;
    private String email;
    private String phoneNumber;
    private String courseShortInfo;
    private String courseInfoTitle;
    private String courseInfo;
    private String courseInfoUrl;
    private BigDecimal price;
    private Long userId;
}