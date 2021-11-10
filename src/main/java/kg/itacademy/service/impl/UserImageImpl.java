package kg.itacademy.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.itacademy.entity.UserImage;
import kg.itacademy.repository.UserImageRepository;
import kg.itacademy.service.UserImageService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserImageImpl implements UserImageService {

    private static final String CLOUDINARY_URL = "cloudinary://827555593978177:78pUgYEkWqkpkugwcNsNwSUyD-o@dv7jsl0n7";

    private UserImageRepository userImageRepository;

    private UserService userService;

    @Override
    public UserImage create(UserImage userImage) throws IllegalArgumentException {

        return userImageRepository.save(userImage);
    }

    @Override
    public UserImage getById(Long id) {
        return userImageRepository.getById(id);
    }

    @Override
    public List<UserImage> getAll() {
        return userImageRepository.findAll();
    }

    @Override
    public UserImage update(UserImage userImage) throws IllegalArgumentException {
        return userImageRepository.save(userImage);
    }

    @Override
    public String saveImageInCloudinary(MultipartFile multipartFile) {
        File file;
        try {
            file = Files.createTempFile(System.currentTimeMillis() + "",
                    multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().length() - 4)).toFile();
            multipartFile.transferTo(file);
            Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return ((String) uploadResult.get("url"));

        } catch (IOException e) {
            log.error("ImageService.createImage: " + e.getMessage());
            return null;
        }
    }

    @Override
    public UserImage saveImage(MultipartFile file) {
        UserImage userImage = new UserImage();
        String savedImageUrl = saveImageInCloudinary(file);
        userImage.setUserImageUrl(savedImageUrl);
        userImage.setUser(userService.getCurrentUser());
        return create(userImage);
    }

    @Override
    public UserImage updateImage(MultipartFile file) throws IllegalArgumentException {
        UserImage updateAvatar = userImageRepository.findByUser_Id(userService.getCurrentUser().getId());
        updateAvatar.setUserImageUrl(saveImageInCloudinary(file));
        updateAvatar.setUser(userService.getCurrentUser());
        return update(updateAvatar);
    }

    @Override
    public UserImage deleteImage(String token) {
        try {
            new Cloudinary(CLOUDINARY_URL).uploader().deleteByToken(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("");
        }
        UserImage deleteAvatar = userImageRepository.findByUser_Id(userService.getCurrentUser().getId());
        if (deleteAvatar == null)
            throw new IllegalArgumentException("Изображение профиля не существует");
        userImageRepository.delete(deleteAvatar);
        return deleteAvatar;
    }
}