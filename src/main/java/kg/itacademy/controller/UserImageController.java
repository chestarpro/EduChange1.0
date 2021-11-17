package kg.itacademy.controller;

import kg.itacademy.model.ResponseMessage;
import kg.itacademy.model.UserImageModel;
import kg.itacademy.service.UserImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
public class UserImageController {

    @Autowired
    private UserImageService userImageService;

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
                .prepareSuccessMessage(userImageService.updateImage(file));
    }

    @DeleteMapping("/delete/{url}")
    public ResponseMessage<UserImageModel> delete(@PathVariable String url) {
        return new ResponseMessage<UserImageModel>()
                .prepareSuccessMessage(userImageService.deleteImage(url));
    }
}