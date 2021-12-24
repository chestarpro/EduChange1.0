package kg.itacademy.model.user;

import kg.itacademy.model.balance.UserBalanceModel;
import kg.itacademy.model.course.CourseDataModel;
import kg.itacademy.model.userImage.UserImageModel;
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
    private UserModelToSend userModelToSend;
    private UserBalanceModel userBalanceModel;
    private UserImageModel userImageModel;
    private List<CourseDataModel> userCreateCourseModels;
    private List<CourseDataModel> userPurchasedCourseModels;
}