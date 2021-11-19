package kg.itacademy.service.impl;

import com.cloudinary.Cloudinary;
import kg.itacademy.converter.CourseImageConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.CourseImage;
import kg.itacademy.exception.ApiErrorException;
import kg.itacademy.model.CourseImageModel;
import kg.itacademy.repository.CourseImageRepository;
import kg.itacademy.service.CourseImageService;

import kg.itacademy.service.UserImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseImageServiceImpl implements CourseImageService {

    private static final String CLOUDINARY_URL = "cloudinary://827555593978177:78pUgYEkWqkpkugwcNsNwSUyD-o@dv7jsl0n7";

    private final CourseImageRepository courseImageRepository;

    private final UserImageService userImageService;

    private final CourseImageConverter CONVERTER = new CourseImageConverter();

    @Override
    public CourseImage save(CourseImage courseImage) {
        return courseImageRepository.save(courseImage);
    }

    @Override
    public CourseImageModel createCourseImage(MultipartFile multipartFile, Long courseId) {
        CourseImage courseImage = new CourseImage();
        String savedImageUrl = userImageService.saveImageInCloudinary(multipartFile);
        courseImage.setCourseImageUrl(savedImageUrl);
        Course course = new Course();
        course.setId(courseId);
        courseImage.setCourse(course);
        return CONVERTER.convertFromEntity(save(courseImage));
    }

    @Override
    public CourseImage getById(Long id) {
        return courseImageRepository.findById(id).orElse(null);
    }

    @Override
    public CourseImageModel getCourseImageModelById(Long id) {
        return CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public CourseImageModel getCourseImageModelByCourseId(Long courseId) {
        CourseImage courseImage = courseImageRepository.findByCourse_Id(courseId).orElse(null);
        return CONVERTER.convertFromEntity(courseImage);
    }

    @Override
    public List<CourseImage> getAll() {
        return courseImageRepository.findAll();
    }

    @Override
    public List<CourseImageModel> getAllUserImageModel() {
        return getAll().stream().map(CONVERTER::convertFromEntity).collect(Collectors.toList());
    }

    @Override
    public CourseImageModel updateImage(MultipartFile multipartFile, Long id) {

        try {
            CourseImage updateCourseImage = getById(id);

            new Cloudinary(CLOUDINARY_URL).uploader().deleteByToken(updateCourseImage.getCourseImageUrl());

            updateCourseImage.setCourseImageUrl(userImageService.saveImageInCloudinary(multipartFile));
            return new CourseImageConverter().convertFromEntity(courseImageRepository.save(updateCourseImage));
        } catch (Exception e) {
            throw new ApiErrorException(e.getMessage());
        }

    }

    @Override
    public CourseImageModel deleteImage(Long id) {
        try {
            CourseImage deleteCourseImage = getById(id);
            new Cloudinary(CLOUDINARY_URL).uploader().deleteByToken(deleteCourseImage.getCourseImageUrl());
            courseImageRepository.delete(deleteCourseImage);
            return new CourseImageConverter().convertFromEntity(deleteCourseImage);
        } catch (Exception e) {
            throw new ApiErrorException(e.getMessage());
        }
    }
}