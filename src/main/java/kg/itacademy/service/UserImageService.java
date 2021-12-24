package kg.itacademy.service;

import kg.itacademy.entity.UserImage;
import kg.itacademy.model.userImage.UserImageModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserImageService extends BaseService<UserImage> {
    UserImageModel createUserImage(MultipartFile file);

    UserImageModel updateUserImage(MultipartFile file);

    UserImageModel deleteImage();

    UserImageModel getUserImageModelById(Long id);

    UserImageModel getUserImageModelByUserId(Long userId);

    UserImage getUserImageByUserId(Long userId);

    List<UserImageModel> getAllUserImageModel();
}