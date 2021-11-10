package kg.itacademy.service.impl;

import kg.itacademy.entity.UserAuthorizLog;
import kg.itacademy.repository.UserAuthorizLogRepository;
import kg.itacademy.service.UserAuthrizLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthorizLogServiceImpl implements UserAuthrizLogService {

    private final UserAuthorizLogRepository userAuthorizLogRepository;


    @Override
    public UserAuthorizLog create(UserAuthorizLog userAuthorizLog) {
        return userAuthorizLogRepository.save(userAuthorizLog);
    }

    @Override
    public UserAuthorizLog getById(Long id) {
        return userAuthorizLogRepository.getById(id);
    }

    @Override
    public List<UserAuthorizLog> getAll() {
        return userAuthorizLogRepository.findAll();
    }

    @Override
    public UserAuthorizLog update(UserAuthorizLog userAuthorizLog) {
        return null;
    }
}
