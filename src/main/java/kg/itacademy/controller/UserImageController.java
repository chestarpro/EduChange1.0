package kg.itacademy.controller;

import kg.itacademy.converter.UserImageConverter;
import kg.itacademy.entity.UserImage;
import kg.itacademy.model.ResponseMessage;
import kg.itacademy.model.UserImageModel;
import kg.itacademy.service.UserImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/image")
public class UserImageController {

    @Autowired
    private UserImageService userImageService;

    @GetMapping("/get-all")
    public List<UserImage> getAll() {
        return userImageService.getAll();
    }

    @PostMapping("/create")
    public ResponseMessage<UserImageModel> saveAvatar(@RequestParam(name = "file") MultipartFile file) {

        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(new UserImageConverter()
                        .convertFromEntity(userImageService.saveImage(file)));

    }

    @PutMapping("/update")
    public ResponseMessage<UserImageModel> updateAvatar(@RequestParam(name = "file") MultipartFile file) {

        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(new UserImageConverter()
                        .convertFromEntity(userImageService.updateImage(file)));

    }

    @DeleteMapping("/delete/{token}")
    public ResponseMessage<UserImageModel> deleteAvatar(@PathVariable String token) {

        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(new UserImageConverter()
                        .convertFromEntity(userImageService.deleteImage(token)));
    }
}