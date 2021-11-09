package kg.itacademy.service;

import kg.itacademy.entity.UserImage;
import org.springframework.web.multipart.MultipartFile;

public interface UserImageService extends BaseService<UserImage> {
    String saveImageInCloudinary(MultipartFile multipartFile);
//    UserImage saveAvatar(String url);
    UserImage saveImage(MultipartFile file);
    UserImage updateImage(MultipartFile file);
    UserImage deleteImage(String url);
}
