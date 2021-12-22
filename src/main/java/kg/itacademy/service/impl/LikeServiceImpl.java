package kg.itacademy.service.impl;

import kg.itacademy.converter.LikeConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.Like;
import kg.itacademy.entity.User;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.LikeModel;
import kg.itacademy.repository.LikeRepository;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.LikeService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;
    private final LikeRepository likeRepository;
    private final LikeConverter likeConverter;

    @Override
    public Like save(Like like) {
        return likeRepository.save(like);
    }

    @Override
    public LikeModel createLikeByCourseId(Long courseId) {
        User user = userService.getCurrentUser();
        Like like = likeRepository
                .findByCourse_IdAndUser_Id(courseId, user.getId())
                .orElse(null);

        if (like != null)
            throw new ApiFailException("Like is already exists");

        Course course = courseService.getById(courseId);
        like = new Like();
        like.setCourse(course);
        like.setUser(user);
        save(like);
        return likeConverter.convertFromEntity(like);
    }

    @Override
    public Like getById(Long id) {
        return likeRepository.findById(id).orElse(null);
    }

    @Override
    public LikeModel getLikeModelById(Long id) {
        return likeConverter.convertFromEntity(getById(id));
    }

    @Override
    public List<Like> getAll() {
        return likeRepository.findAll();
    }

    @Override
    public List<LikeModel> getAllLikeModelByCourseId(Long id) {
        return likeRepository
                .findAllByCourse_Id(id)
                .stream()
                .map(likeConverter::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public LikeModel deleteLike(Long courseId) {
        Long currentUserId = userService.getCurrentUser().getId();
        Like dataLike = likeRepository.findByCourse_IdAndUser_Id(courseId, currentUserId).orElse(null);
        if (dataLike == null)
            throw new ApiFailException("Like not found");

        likeRepository.delete(dataLike);
        return likeConverter.convertFromEntity(dataLike);
    }
}