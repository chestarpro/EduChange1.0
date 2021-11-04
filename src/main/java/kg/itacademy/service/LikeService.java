package kg.itacademy.service;

import kg.itacademy.entity.Like;

public interface LikeService extends BaseService<Like> {
    Like deleteLike();
}
