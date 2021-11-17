package kg.itacademy.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.itacademy.converter.UserImageConverter;
import kg.itacademy.entity.UserImage;
import kg.itacademy.exception.ApiErrorException;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.UserImageModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserImageImpl implements UserImageService {

    private static final String CLOUDINARY_URL = "cloudinary://827555593978177:78pUgYEkWqkpkugwcNsNwSUyD-o@dv7jsl0n7";

    private UserImageRepository userImageRepository;

    private UserService userService;

    @Override
    public UserImage save(UserImage userImage) {

        return userImageRepository.save(userImage);
    }

    @Override
    public UserImageModel createUserImage(MultipartFile file) {
        UserImage userImage = new UserImage();
        String savedImageUrl = saveImageInCloudinary(file);
        userImage.setUserImageUrl(savedImageUrl);
        userImage.setUser(userService.getCurrentUser());
        return new UserImageConverter().convertFromEntity(save(userImage));
    }

    @Override
    public String saveImageInCloudinary(MultipartFile multipartFile) {
        try {
            if (multipartFile.getOriginalFilename() == null)
                throw new ApiFailException("Файл пуст");

            File file = Files.createTempFile(System.currentTimeMillis() + "",
                    Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().indexOf('.'))).toFile();
            multipartFile.transferTo(file);
            Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return ((String) uploadResult.get("url"));
        } catch (IOException e) {
            throw new ApiFailException(e.getMessage());
        }
    }

    @Override
    public UserImage getById(Long id) {
        return userImageRepository.findById(id).orElse(null);
    }

    @Override
    public UserImageModel getUserImageModelById(Long id) {
        return new UserImageConverter().convertFromEntity(getById(id));
    }

    @Override
    public List<UserImage> getAll() {
        return userImageRepository.findAll();
    }

    @Override
    public List<UserImageModel> getAllUserImageModel() {
        List<UserImageModel> imageModels = new ArrayList<>();
        for (UserImage userImage : getAll())
            imageModels.add(new UserImageConverter().convertFromEntity(userImage));
        return imageModels;
    }


    @Override
    public UserImage update(UserImage userImage) {
        return userImageRepository.save(userImage);
    }

    @Override
    public UserImageModel updateImage(MultipartFile file) {
        try {
            UserImage updateAvatar = userImageRepository.findByUser_Id(userService.getCurrentUser().getId());
            new Cloudinary(CLOUDINARY_URL).uploader().deleteByToken(updateAvatar.getUserImageUrl());
            updateAvatar.setUserImageUrl(saveImageInCloudinary(file));
            updateAvatar.setUser(userService.getCurrentUser());
            return new UserImageConverter().convertFromEntity(update(updateAvatar));
        } catch (Exception e) {
            throw new ApiErrorException(e.getMessage());
        }
    }

    @Override
    public UserImageModel deleteImage(String url) {
        try {
            new Cloudinary(CLOUDINARY_URL).uploader().deleteByToken(url);
            UserImage deleteAvatar = userImageRepository.findByUser_Id(userService.getCurrentUser().getId());
            userImageRepository.delete(deleteAvatar);
            return new UserImageConverter().convertFromEntity(deleteAvatar);
        } catch (Exception e) {
            throw new ApiErrorException(e.getMessage());
        }
    }
}