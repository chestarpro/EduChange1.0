package kg.itacademy.service.impl;

import kg.itacademy.converter.UserImageConverter;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserImage;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.userImage.UserImageModel;
import kg.itacademy.repository.UserImageRepository;
import kg.itacademy.service.UserImageService;
import kg.itacademy.service.UserService;
import kg.itacademy.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserImageServiceImpl implements UserImageService {
    @Autowired
    private UserService USER_SERVICE;
    private static final String CLOUDINARY_URL = "cloudinary://349958975956714:wJERqVH-qai2mlMdGYqzSY__kFM@du9qubfii";
    private final UserImageRepository USER_IMAGE_REPOSITORY;
    private final UserImageConverter USER_IMAGE_CONVERTER;

    @Override
    public UserImage save(UserImage userImage) {
        return USER_IMAGE_REPOSITORY.save(userImage);
    }

    @Override
    public UserImageModel createUserImage(MultipartFile file) {
        User user = USER_SERVICE.getCurrentUser();

        UserImage userImage = USER_IMAGE_REPOSITORY.findByUser_Id(user.getId());

        if (userImage != null)
            throw new ApiFailException("User image is already");

        String savedImageUrl = ImageUtil.saveImageInCloudinary(file);
        userImage = new UserImage();
        userImage.setUserImageUrl(savedImageUrl);
        userImage.setUser(user);
        save(userImage);
        return USER_IMAGE_CONVERTER.convertFromEntity(userImage);
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
    public UserImage getUserImageByUserId(Long userId) {
        return USER_IMAGE_REPOSITORY.findByUser_Id(userId);
    }

    @Override
    public UserImageModel getUserImageModelByUserId(Long userId) {
        return USER_IMAGE_CONVERTER.convertFromEntity(getUserImageByUserId(userId));
    }

    @Override
    public List<UserImage> getAll() {
        return USER_IMAGE_REPOSITORY.findAll();
    }

    @Override
    public List<UserImageModel> getAllUserImageModel() {
        return getAll().stream()
                .map(USER_IMAGE_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UserImageModel updateUserImage(MultipartFile file) {
        UserImage updateUserImage = getDataUserImageByCurrentUser();
        String updateImageUrl = ImageUtil.saveImageInCloudinary(file);
        updateUserImage.setUserImageUrl(updateImageUrl);
        USER_IMAGE_REPOSITORY.save(updateUserImage);
        return USER_IMAGE_CONVERTER.convertFromEntity(updateUserImage);
    }

    @Override
    public UserImageModel deleteImage() {
        UserImage deleteUserImage = getDataUserImageByCurrentUser();
        USER_IMAGE_REPOSITORY.delete(deleteUserImage);
        return USER_IMAGE_CONVERTER.convertFromEntity(deleteUserImage);
    }

    private UserImage getDataUserImageByCurrentUser() {
        Long currentUserId = USER_SERVICE.getCurrentUser().getId();
        UserImage dataUserImage = USER_IMAGE_REPOSITORY.findByUser_Id(currentUserId);

        if (dataUserImage == null)
            throw new ApiFailException("User image by user id " + currentUserId + " not found");

        return dataUserImage;
    }
}