package kg.itacademy.service;

import kg.itacademy.entity.UserImage;
import kg.itacademy.model.user.UserImageModel;
import kg.itacademy.model.user.UserImageUrlModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserImageService extends BaseService<UserImage> {
    String saveImageInCloudinary(MultipartFile multipartFile);
    UserImageModel createUserImage(MultipartFile file);
    UserImageModel updateUserImage(MultipartFile file);
    UserImageModel deleteImage(UserImageUrlModel urlModel);
    UserImageModel getUserImageModelById(Long id);
    UserImageModel getUserImageModelByUserId(Long userId);
    List<UserImageModel> getAllUserImageModel();
}