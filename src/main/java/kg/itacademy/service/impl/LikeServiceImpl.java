package kg.itacademy.service.impl;

import kg.itacademy.converter.LikeConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.Like;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.LikeModel;
import kg.itacademy.repository.LikeRepository;
import kg.itacademy.service.CourseService;
import kg.itacademy.service.LikeService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    private final CourseService courseService;

    private final UserService userService;

    @Override
    public Like save(Like like) {
        Like dataLike = getById(like.getId());
        if (dataLike != null)
            throw new IllegalArgumentException("\"Like\" already exists");
        return likeRepository.save(like);
    }

    @Override
    public LikeModel createLikeByCourseId(Long courseId) {
        Course course = courseService.getById(courseId);
        Like like = new Like();
        like.setCourse(course);
        like.setUser(userService.getCurrentUser());

        return new LikeConverter().convertFromEntity(save(like));
    }

    @Override
    public Like getById(Long id) {
        Like like = likeRepository.findById(id).orElse(null);
        if (like == null)
            throw new ApiFailException("Like by ID(" + id + ") not found");
        return like;
    }

    @Override
    public LikeModel getLikeModelById(Long id) {
        return new LikeConverter().convertFromEntity(getById(id));
    }

    @Override
    public List<Like> getAll() {
        return null;
    }

    @Override
    public List<LikeModel> getAllLikeModelByCourseId(Long id) {
        List<Like> likes = likeRepository.findAllByCourse_Id(id);
        if (!likes.isEmpty()) {
            LikeConverter converter = new LikeConverter();
            return likes.stream()
                    .map(converter::convertFromEntity)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Like update(Like like) {
        return null;
    }

    @Override
    public LikeModel deleteLike(Long id) {
        Like like = getById(id);
        likeRepository.delete(like);
        return new LikeConverter().convertFromEntity(like);
    }
}