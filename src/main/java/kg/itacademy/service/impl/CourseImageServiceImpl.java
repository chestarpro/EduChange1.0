package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseImageConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.CourseImage;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.CourseImageModel;
import kg.itacademy.repository.CourseImageRepository;
import kg.itacademy.service.CourseImageService;

import kg.itacademy.service.UserImageService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseImageServiceImpl implements CourseImageService {
    @Autowired
    private UserService USER_SERVICE;
    @Autowired
    private CourseServiceImpl COURSE_SERVICE;
    private final CourseImageRepository COURSE_IMAGE_REPOSITORY;
    private final UserImageService USER_IMAGE_SERVICE;
    private final CourseImageConverter COURSE_IMAGER_CONVERTER;

    @Override
    public CourseImage save(CourseImage courseImage) {
        return COURSE_IMAGE_REPOSITORY.save(courseImage);
    }

    @Override
    public CourseImageModel createCourseImage(MultipartFile multipartFile, Long courseId) {
        Course course = checkDataCourseByCourseId(courseId);

        CourseImage courseImage = COURSE_IMAGE_REPOSITORY.findByCourse_Id(courseId).orElse(null);

        if (courseImage != null)
            throw new ApiFailException("Course image is already");

        String savedImageUrl = USER_IMAGE_SERVICE.saveImageInCloudinary(multipartFile);
        courseImage = new CourseImage();
        courseImage.setCourseImageUrl(savedImageUrl);
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
        CourseImage updateCourseImage = checkDataCourseImageById(id);
        String updateImageUrl = USER_IMAGE_SERVICE.saveImageInCloudinary(multipartFile);
        updateCourseImage.setCourseImageUrl(updateImageUrl);
        COURSE_IMAGE_REPOSITORY.save(updateCourseImage);
        return COURSE_IMAGER_CONVERTER.convertFromEntity(updateCourseImage);
    }

    @Override
    public CourseImageModel deleteImage(Long id) {
        CourseImage deleteCourseImage = checkDataCourseImageById(id);
        COURSE_IMAGE_REPOSITORY.delete(deleteCourseImage);
        return COURSE_IMAGER_CONVERTER.convertFromEntity(deleteCourseImage);
    }

    private Course checkDataCourseByCourseId(Long courseId) {
        if (courseId == null)
            throw new ApiFailException("Course id not specified");

        Course course = COURSE_SERVICE.getById(courseId);

        if (course == null)
            throw new ApiFailException("Course by id " + courseId + " not found");

        checkAccess(course.getUser().getId());
        return course;
    }

    private CourseImage checkDataCourseImageById(Long id) {
        if (id == null)
            throw new ApiFailException("Course image id not specified");

        CourseImage dataCourseImage = getById(id);

        if (dataCourseImage == null)
            throw new ApiFailException("Course image by id " + id + " not found");

        Long authorCourseId = dataCourseImage.getCourse().getUser().getId();
        checkAccess(authorCourseId);
        return dataCourseImage;
    }

    private void checkAccess(Long authorCourseId) {
        Long currentUserId = USER_SERVICE.getCurrentUser().getId();

        if (!currentUserId.equals(authorCourseId))
            throw new ApiFailException("Access is denied");
    }
}