package kg.itacademy.service.impl;

import kg.itacademy.converter.LikeConverter;
import kg.itacademy.entity.Course;
import kg.itacademy.entity.Like;
import kg.itacademy.entity.User;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.course.LikeModel;
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

    private final LikeRepository LIKE_REPOSITORY;
    private final CourseService COURSE_SERVICE;
    private final UserService USER_SERVICE;
    private final LikeConverter LIKE_CONVERTER;

    @Override
    public Like save(Like like) {

        return LIKE_REPOSITORY.save(like);
    }

    @Override
    public LikeModel createLikeByCourseId(Long courseId) {
        User user = USER_SERVICE.getCurrentUser();
        Like dataLike = LIKE_REPOSITORY
                .findByCourse_IdAndUser_Id(courseId, user.getId())
                .orElse(null);

        if (dataLike != null)
            throw new ApiFailException("\"Like\" already exists");
        Course course = COURSE_SERVICE.getById(courseId);
        Like like = new Like();
        like.setCourse(course);
        like.setUser(user);

        return LIKE_CONVERTER.convertFromEntity(save(like));
    }

    @Override
    public Like getById(Long id) {
        return LIKE_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public LikeModel getLikeModelById(Long id) {
        return LIKE_CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public List<Like> getAll() {
        return null;
    }

    @Override
    public List<LikeModel> getAllLikeModelByCourseId(Long id) {
        return LIKE_REPOSITORY.findAllByCourse_Id(id).stream()
                .map(LIKE_CONVERTER::convertFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public LikeModel deleteLike(Long id) {
        Like like = getById(id);
        if (like == null)
            throw new ApiFailException("\"Like\" by id " + id + "not found");
        LIKE_REPOSITORY.delete(like);
        return LIKE_CONVERTER.convertFromEntity(like);
    }
}