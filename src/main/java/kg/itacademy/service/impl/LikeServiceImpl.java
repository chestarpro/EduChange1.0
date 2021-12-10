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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    private final CourseService courseService;

    private final UserService userService;

    private final LikeConverter CONVERTER = new LikeConverter();

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

        return CONVERTER.convertFromEntity(save(like));
    }

    @Override
    public Like getById(Long id) {
        return likeRepository.findById(id).orElse(null);
    }

    @Override
    public LikeModel getLikeModelById(Long id) {
        return CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public List<Like> getAll() {
        return null;
    }

    @Override
    public List<LikeModel> getAllLikeModelByCourseId(Long id) {
        return likeRepository.findAllByCourse_Id(id).stream()
                .map(CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public LikeModel deleteLike(Long id) {
        Like like = getById(id);
        if (like == null)
            throw new ApiFailException("\"Like\" by id " + id + "not found");
        likeRepository.delete(like);
        return CONVERTER.convertFromEntity(like);
    }
}