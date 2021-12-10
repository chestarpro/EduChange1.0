package kg.itacademy.model.user;

import kg.itacademy.model.course.CourseModel;
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
    private List<CourseModel> userCreateCourses;
    private List<CourseModel> userPurchasedCourses;
}