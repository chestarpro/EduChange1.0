package kg.itacademy.service;

import kg.itacademy.entity.UserImage;
import kg.itacademy.model.UserImageModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserImageService extends BaseService<UserImage> {
    String saveImageInCloudinary(MultipartFile multipartFile);
    UserImageModel createUserImage(MultipartFile file);
    UserImageModel updateImage(MultipartFile file);
    UserImageModel deleteImage(String url);
    UserImageModel getUserImageModelById(Long id);
    List<UserImageModel> getAllUserImageModel();
    //    UserImage saveAvatar(String url);
}
