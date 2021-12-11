package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseImageConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.CourseImage;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.courseImage.CourseImageModel;
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

    private final CourseImageRepository COURSE_IMAGE_REPOSITORY;
    private final UserImageService USER_IMAGE_REPOSITORY;
    private final CourseImageConverter COURSE_IMAGER_CONVERTER;

    @Override
    public CourseImage save(CourseImage courseImage) {
        return COURSE_IMAGE_REPOSITORY.save(courseImage);
    }

    @Override
    public CourseImageModel createCourseImage(MultipartFile multipartFile, Long courseId) {
        String savedImageUrl = USER_IMAGE_REPOSITORY.saveImageInCloudinary(multipartFile);
        CourseImage courseImage = new CourseImage();
        courseImage.setCourseImageUrl(savedImageUrl);
        Course course = new Course();
        course.setId(courseId);
        courseImage.setCourse(course);
        return COURSE_IMAGER_CONVERTER.convertFromEntity(save(courseImage));
    }

    @Override
    public CourseImage getById(Long id) {
        return COURSE_IMAGE_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public CourseImageModel getCourseImageModelById(Long id) {
        return COURSE_IMAGER_CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public CourseImageModel getCourseImageModelByCourseId(Long courseId) {
        CourseImage courseImage = COURSE_IMAGE_REPOSITORY.findByCourse_Id(courseId).orElse(null);
        return COURSE_IMAGER_CONVERTER.convertFromEntity(courseImage);
    }

    @Override
    public List<CourseImage> getAll() {
        return COURSE_IMAGE_REPOSITORY.findAll();
    }

    @Override
    public List<CourseImageModel> getAllUserImageModel() {
        return getAll()
                .stream()
                .map(COURSE_IMAGER_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CourseImageModel updateImage(MultipartFile multipartFile, Long id) {
        CourseImage updateCourseImage = getById(id);

        if (updateCourseImage == null)
            throw new ApiFailException("Course image by id " + id + " not found");

        updateCourseImage.setCourseImageUrl(USER_IMAGE_REPOSITORY.saveImageInCloudinary(multipartFile));

        COURSE_IMAGE_REPOSITORY.save(updateCourseImage);

        return COURSE_IMAGER_CONVERTER.convertFromEntity(updateCourseImage);
    }

    @Override
    public CourseImageModel deleteImage(Long id) {
        CourseImage deleteCourseImage = getById(id);
        if (deleteCourseImage == null)
            throw new ApiFailException("Course image by id " + id + " not found");

        COURSE_IMAGE_REPOSITORY.delete(deleteCourseImage);

        return COURSE_IMAGER_CONVERTER.convertFromEntity(deleteCourseImage);
    }
}