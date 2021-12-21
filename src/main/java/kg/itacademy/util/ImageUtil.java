package kg.itacademy.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.itacademy.exception.ApiErrorException;
import kg.itacademy.exception.ApiFailException;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

@UtilityClass
public class ImageUtil {

    private static final String CLOUDINARY_URL = "cloudinary://349958975956714:wJERqVH-qai2mlMdGYqzSY__kFM@du9qubfii";

    public static String saveImageInCloudinary(MultipartFile multipartFile) {
        try {
            if (multipartFile.getOriginalFilename() == null || multipartFile.getOriginalFilename().isEmpty())
                throw new ApiFailException("File name not specified");

            File file = Files.createTempFile(System.currentTimeMillis() + "",
                    Objects.requireNonNull(multipartFile.getOriginalFilename())
                            .substring(multipartFile.getOriginalFilename().indexOf('.'))).toFile();

            multipartFile.transferTo(file);
            Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return ((String) uploadResult.get("url"));
        } catch (IOException e) {
            throw new ApiErrorException(e.getMessage());
        }
    }
}