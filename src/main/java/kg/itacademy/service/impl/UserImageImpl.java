package kg.itacademy.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.itacademy.converter.UserImageConverter;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserImage;
import kg.itacademy.exception.ApiErrorException;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.user.UserImageModel;
import kg.itacademy.model.user.UserImageUrlModel;
import kg.itacademy.repository.UserImageRepository;
import kg.itacademy.service.UserImageService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserImageImpl implements UserImageService {

    private static final String CLOUDINARY_URL = "cloudinary://349958975956714:wJERqVH-qai2mlMdGYqzSY__kFM@du9qubfii";
    private final UserImageRepository USER_IMAGE_REPOSITORY;
    private final UserImageConverter USER_IMAGE_CONVERTER;

    @Autowired
    private UserService userService;

    @Override
    public UserImage save(UserImage userImage) {
        return USER_IMAGE_REPOSITORY.save(userImage);
    }

    @Override
    public UserImageModel createUserImage(MultipartFile file) {
        String savedImageUrl = saveImageInCloudinary(file);
        UserImage userImage = new UserImage();
        userImage.setUserImageUrl(savedImageUrl);
        userImage.setUser(userService.getCurrentUser());
        return USER_IMAGE_CONVERTER.convertFromEntity(save(userImage));
    }

    @Override
    public String saveImageInCloudinary(MultipartFile multipartFile) {
        try {
            if (multipartFile.getOriginalFilename() == null || multipartFile.getOriginalFilename().isEmpty())
                throw new ApiFailException("File name not specified");

            File file = Files.createTempFile(System.currentTimeMillis() + "",
                    Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().indexOf('.'))).toFile();
            multipartFile.transferTo(file);
            Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

            return ((String) uploadResult.get("url"));
        } catch (IOException e) {
            throw new ApiErrorException(e.getMessage());
        }
    }

    @Override
    public UserImage getById(Long id) {
        return USER_IMAGE_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public UserImageModel getUserImageModelById(Long id) {
        return USER_IMAGE_CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public UserImageModel getUserImageModelByUserId(Long userId) {
        UserImage userImage = USER_IMAGE_REPOSITORY.findByUser_Id(userId);
        return USER_IMAGE_CONVERTER.convertFromEntity(userImage);
    }

    @Override
    public List<UserImage> getAll() {
        return USER_IMAGE_REPOSITORY.findAll();
    }

    @Override
    public List<UserImageModel> getAllUserImageModel() {
        return getAll().stream()
                .map(USER_IMAGE_CONVERTER::convertFromEntity).collect(Collectors.toList());
    }

    public UserImage update(UserImage userImage) {
        if (userImage.getId() == null)
            throw new ApiFailException("User image id not specified");

        return USER_IMAGE_REPOSITORY.save(userImage);
    }

    @Override
    public UserImageModel updateUserImage(MultipartFile file) {
        try {
            Long userId = userService.getCurrentUser().getId();
            UserImage updateUserImage = USER_IMAGE_REPOSITORY.findByUser_Id(userId);

            if (updateUserImage == null)
                throw new ApiFailException("User image by user id (" + userId + ") not found");
            updateUserImage.setUserImageUrl(saveImageInCloudinary(file));

            return USER_IMAGE_CONVERTER.convertFromEntity(update(updateUserImage));
        } catch (Exception e) {
            throw new ApiErrorException(e.getMessage());
        }
    }

    @Override
    public UserImageModel deleteImage(UserImageUrlModel urlModel) {
        try {
            Long userId = userService.getCurrentUser().getId();
            UserImage deleteUserImage = USER_IMAGE_REPOSITORY.findByUser_Id(userId);

            if (deleteUserImage == null)
                throw new ApiFailException("User image by user id (" + userId + ") not found");

            USER_IMAGE_REPOSITORY.delete(deleteUserImage);

            return USER_IMAGE_CONVERTER.convertFromEntity(deleteUserImage);
        } catch (Exception e) {
            throw new ApiErrorException(e.getMessage());
        }
    }
}