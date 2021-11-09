package kg.itacademy.controller;

import kg.itacademy.converter.UserImageConverter;
import kg.itacademy.entity.UserImage;
import kg.itacademy.model.ResponseMessage;
import kg.itacademy.model.UserImageModel;
import kg.itacademy.service.UserImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseMessage<UserImageModel>saveAvatar(@RequestParam(name = "file") MultipartFile file) {
        ResponseMessage<UserImageModel> responseMessage = new ResponseMessage<>();
        try {
            return responseMessage
                    .prepareSuccessMessage(new UserImageConverter()
                            .convertFromEntity(userImageService.saveImage(file)));
        } catch (IllegalArgumentException e) {
            return responseMessage.prepareFailMessage(HttpStatus.BAD_REQUEST.value(), "Не верный формат");
        } catch (Exception e) {
            return responseMessage.prepareFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseMessage<UserImageModel> updateAvatar(@RequestParam(name = "file") MultipartFile file) {
        ResponseMessage<UserImageModel> responseMessage = new ResponseMessage<>();
        try {
            return responseMessage
                    .prepareSuccessMessage(new UserImageConverter()
                            .convertFromEntity(userImageService.updateImage(file)));
        } catch (IllegalArgumentException e) {
            return responseMessage.prepareFailMessage(HttpStatus.BAD_REQUEST.value(), "Не верный формат");
        } catch (Exception e) {
            return responseMessage.prepareFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @DeleteMapping("/delete/{token}")
    public ResponseMessage<UserImageModel> deleteAvatar(@PathVariable String token) {
        ResponseMessage<UserImageModel> responseMessage = new ResponseMessage<>();
        try {
            return responseMessage.
                    prepareSuccessMessage(new UserImageConverter()
                            .convertFromEntity(userImageService.deleteImage(token)));
        } catch (IllegalArgumentException e) {
            return responseMessage.prepareFailMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return responseMessage.prepareFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}