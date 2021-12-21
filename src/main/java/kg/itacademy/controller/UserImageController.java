package kg.itacademy.controller;

import kg.itacademy.util.ResponseMessage;
import kg.itacademy.model.userImage.UserImageModel;
import kg.itacademy.service.UserImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user-image")
@RequiredArgsConstructor
public class UserImageController {
    private final UserImageService userImageService;

    @PostMapping("/create")
    public ResponseMessage<UserImageModel> save(@RequestParam(name = "file") MultipartFile file) {
        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(userImageService.createUserImage(file));
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

    @DeleteMapping("/delete")
    public ResponseMessage<UserImageModel> delete() {
        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(userImageService.deleteImage());
    }
}