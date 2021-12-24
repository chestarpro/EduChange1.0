package kg.itacademy.service.impl;

import kg.itacademy.converter.CourseImageConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.CourseImage;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.CourseImageModel;
import kg.itacademy.repository.CourseImageRepository;
import kg.itacademy.service.CourseImageService;
import kg.itacademy.service.UserService;
import kg.itacademy.util.ImageUtil;
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
    private UserService userService;
    @Autowired
    private CourseServiceImpl courseService;
    private final CourseImageRepository courseImageRepository;
    private final CourseImageConverter courseImageConverter;

    @Override
    public CourseImage save(CourseImage courseImage) {
        return courseImageRepository.save(courseImage);
    }

    @Override
    public CourseImageModel createCourseImage(MultipartFile multipartFile, Long courseId) {
        Course course = getDataCourseByCourseIdWithCheckAccess(courseId);

        CourseImage courseImage = courseImageRepository.findByCourse_Id(courseId).orElse(null);

        if (courseImage != null)
            throw new ApiFailException("Изображение курса готово");

        String savedImageUrl = ImageUtil.saveImageInCloudinary(multipartFile);
        courseImage = new CourseImage();
        courseImage.setCourseImageUrl(savedImageUrl);
        courseImage.setCourse(course);
        save(courseImage);

        return courseImageConverter.convertFromEntity(courseImage);
    }

    @Override
    public CourseImage getById(Long id) {
        return courseImageRepository.findById(id).orElse(null);
    }

    @Override
    public CourseImageModel getCourseImageModelById(Long id) {
        return courseImageConverter.convertFromEntity(getById(id));
    }

    @Override
    public CourseImageModel getCourseImageModelByCourseId(Long courseId) {
        CourseImage courseImage = courseImageRepository.findByCourse_Id(courseId).orElse(null);
        return courseImageConverter.convertFromEntity(courseImage);
    }

    @Override
    public List<CourseImage> getAll() {
        return courseImageRepository.findAll();
    }

    @Override
    public List<CourseImageModel> getAllUserImageModel() {
        return getAll()
                .stream()
                .map(courseImageConverter::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CourseImageModel updateImage(MultipartFile multipartFile, Long id) {
        CourseImage updateCourseImage = getDataCourseImageByIdWithCheckAccess(id);
        String updateImageUrl = ImageUtil.saveImageInCloudinary(multipartFile);
        updateCourseImage.setCourseImageUrl(updateImageUrl);
        courseImageRepository.save(updateCourseImage);
        return courseImageConverter.convertFromEntity(updateCourseImage);
    }

    @Override
    public CourseImageModel deleteImage(Long id) {
        CourseImage deleteCourseImage = getDataCourseImageByIdWithCheckAccess(id);
        courseImageRepository.delete(deleteCourseImage);
        return courseImageConverter.convertFromEntity(deleteCourseImage);
    }

    private Course getDataCourseByCourseIdWithCheckAccess(Long courseId) {
        if (courseId == null)
            throw new ApiFailException("Не указан ID курса");

        Course course = courseService.getById(courseId);

        if (course == null)
            throw new ApiFailException("Курс под ID " + courseId + " не найден");

        checkAccess(course.getUser().getId());
        return course;
    }

    private CourseImage getDataCourseImageByIdWithCheckAccess(Long id) {
        if (id == null)
            throw new ApiFailException("Не указан ID изображения круса");

        CourseImage dataCourseImage = getById(id);

        if (dataCourseImage == null)
            throw new ApiFailException("Изображение курса под ID " + id + " не найдено");

        Long authorCourseId = dataCourseImage.getCourse().getUser().getId();
        checkAccess(authorCourseId);
        return dataCourseImage;
    }

    private void checkAccess(Long authorCourseId) {
        Long currentUserId = userService.getCurrentUser().getId();

        if (!currentUserId.equals(authorCourseId))
            throw new ApiFailException("Доступ ограничен");
    }
}