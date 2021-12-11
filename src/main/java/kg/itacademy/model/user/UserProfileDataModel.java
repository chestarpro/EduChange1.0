package kg.itacademy.model.user;

import kg.itacademy.model.course.CourseDataModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDataModel {
    private String token;
    private UserModel user;
    private UserBalanceModel userBalance;
    private UserImageModel userImage;
    private List<CourseDataModel> userCreateCourses;
    private List<CourseDataModel> userPurchasedCourses;
}