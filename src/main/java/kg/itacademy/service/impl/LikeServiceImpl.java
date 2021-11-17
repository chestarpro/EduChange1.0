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

import java.util.ArrayList;
import java.util.List;

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
            throw new IllegalArgumentException("Like уже существует");
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
        return likeRepository.findById(id).orElse(null);
    }

    @Override
    public LikeModel getLikeModelById(Long id) {
        return new LikeConverter().convertFromEntity(getById(id));
    }

    @Override
    public List<Like> getAll() {
        return likeRepository.findAll();
    }

    @Override
    public List<LikeModel> getAllLikeModelByCourseId(Long id) {
        List<Like> likes = likeRepository.findAllByCourse_Id(id);
        if (likes == null)
            throw new ApiFailException("Likes по курсу (id: " + id + ") не найдены");
        List<LikeModel> likeModels = new ArrayList<>();
        for (Like like : likes)
            likeModels.add(new LikeConverter().convertFromEntity(like));
        return likeModels;
    }

    @Override
    public Like update(Like like) {
        return null;
    }

    @Override
    public LikeModel deleteLike(Long id) {
        Like like = getById(id);
        if (like == null)
            throw new ApiFailException("Like под id: " + id + " не найден");
        likeRepository.delete(like);
        return new LikeConverter().convertFromEntity(like);
    }
}