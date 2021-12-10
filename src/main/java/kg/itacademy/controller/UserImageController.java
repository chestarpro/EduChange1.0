package kg.itacademy.controller;

import kg.itacademy.model.UserImageUrlModel;
import kg.itacademy.util.ResponseMessage;
import kg.itacademy.model.UserImageModel;
import kg.itacademy.service.UserImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user-image")
public class UserImageController {

    @Autowired
    private UserImageService userImageService;

    @PostMapping("/create/{userId}")
    public ResponseMessage<UserImageModel> save(@RequestParam(name = "file") MultipartFile file, @PathVariable Long userId) {
        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(userImageService.createUserImage(file, userId));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseMessage<UserImageModel> getById(@PathVariable Long id) {
        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(userImageService.getUserImageModelById(id));
    }

    @PutMapping("/update")
    public ResponseMessage<UserImageModel> update(@RequestParam(name = "file") MultipartFile file) {
        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(userImageService.updateUserImage(file));
    }

    @PostMapping("/delete")
    public ResponseMessage<UserImageModel> delete(@RequestBody UserImageUrlModel url) {
        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(userImageService.deleteImage(url));
    }
}