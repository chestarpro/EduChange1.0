package kg.itacademy.service;

import kg.itacademy.entity.CourseImage;
import kg.itacademy.model.CourseImageModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseImageService extends BaseService<CourseImage> {
    CourseImageModel createCourseImage(MultipartFile multipartFile, Long courseId);

    CourseImageModel updateImage(MultipartFile multipartFile, Long id);

    CourseImageModel deleteImage(Long id);

    CourseImageModel getCourseImageModelById(Long id);

    CourseImageModel getCourseImageModelByCourseId(Long courseId);

    List<CourseImageModel> getAllUserImageModel();
}