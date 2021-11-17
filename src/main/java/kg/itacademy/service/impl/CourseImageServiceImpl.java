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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseImageServiceImpl implements CourseImageService {

    private static final String CLOUDINARY_URL = "cloudinary://827555593978177:78pUgYEkWqkpkugwcNsNwSUyD-o@dv7jsl0n7";

    private final CourseImageRepository courseImageRepository;

    private final UserImageService userImageService;

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
        return new CourseImageConverter().convertFromEntity(save(courseImage));
    }

    @Override
    public CourseImage getById(Long id) {
        return courseImageRepository.findById(id).orElse(null);
    }

    @Override
    public CourseImageModel getCourseImageModelById(Long id) {
        return new CourseImageConverter().convertFromEntity(getById(id));
    }

    @Override
    public CourseImageModel getCourseImageModelByCourseId(Long courseId) {
        CourseImage courseImage = courseImageRepository.findByCourse_Id(courseId);
        return new CourseImageConverter().convertFromEntity(courseImage);
    }

    @Override
    public List<CourseImage> getAll() {
        return courseImageRepository.findAll();
    }

    @Override
    public List<CourseImageModel> getAllUserImageModel() {
        List<CourseImageModel> imageModels = new ArrayList<>();
        for (CourseImage courseImage : getAll())
            imageModels.add(new CourseImageConverter().convertFromEntity(courseImage));
        return imageModels;
    }

    @Override
    public CourseImage update(CourseImage courseImage) {
        return courseImageRepository.save(courseImage);
    }

    @Override
    public CourseImageModel updateImage(MultipartFile multipartFile, Long id) {
        CourseImage updateCourseImage;
        try {
            updateCourseImage = getById(id);

            new Cloudinary(CLOUDINARY_URL).uploader().deleteByToken(updateCourseImage.getCourseImageUrl());

            updateCourseImage.setCourseImageUrl(userImageService.saveImageInCloudinary(multipartFile));

        } catch (Exception e) {
            throw new ApiErrorException(e.getMessage());
        }
        return new CourseImageConverter().convertFromEntity(update(updateCourseImage));
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