package kg.itacademy.controller;

import kg.itacademy.model.user.UserImageUrlModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.model.user.UserImageModel;
import kg.itacademy.service.UserImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user-image")
@RequiredArgsConstructor
public class UserImageController {

    private final UserImageService USER_IMAGE_SERVICE;

    @PostMapping("/create")
    public ResponseMessage<UserImageModel> save(@RequestParam(name = "file") MultipartFile file) {
        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(USER_IMAGE_SERVICE.createUserImage(file));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<UserImageModel> getById(@PathVariable Long id) {
        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(USER_IMAGE_SERVICE.getUserImageModelById(id));
    }

    @PutMapping("/update")
    public ResponseMessage<UserImageModel> update(@RequestParam(name = "file") MultipartFile file) {
        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(USER_IMAGE_SERVICE.updateUserImage(file));
    }

    @PostMapping("/delete")
    public ResponseMessage<UserImageModel> delete(@RequestBody UserImageUrlModel url) {
        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(USER_IMAGE_SERVICE.deleteImage(url));
    }
}