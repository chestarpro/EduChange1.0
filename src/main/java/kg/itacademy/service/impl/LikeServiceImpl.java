package kg.itacademy.service.impl;

import kg.itacademy.entity.Like;
import kg.itacademy.repository.LikeRepository;
import kg.itacademy.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    public Like create(Like like) {
        Like dataLike = getById(like.getId());
        if (dataLike != null)
            throw new IllegalArgumentException("Like уже существует");
        return likeRepository.save(like);
    }

    @Override
    public Like getById(Long id) {
        return likeRepository.getById(id);
    }

    @Override
    public List<Like> getAll() {
        return likeRepository.findAll();
    }

    @Override
    public Like update(Like like) {
        return null;
    }

    @Override
    public Like deleteLike(Long id) {
        Like like = getById(id);
        likeRepository.delete(like);
        return like;
    }
}