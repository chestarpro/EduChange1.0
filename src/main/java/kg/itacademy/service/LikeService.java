package kg.itacademy.service;

import kg.itacademy.entity.Like;
import kg.itacademy.model.LikeModel;

import java.util.List;

public interface LikeService extends BaseService<Like> {
    LikeModel createLikeByCourseId(Long courseId);

    LikeModel deleteLike(Long courseId);

    LikeModel getLikeModelById(Long id);

    List<LikeModel> getAllLikeModelByCourseId(Long id);
}