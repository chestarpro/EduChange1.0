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
    public UserAuthorizLog save(UserAuthorizLog userAuthorizLog) {
        return userAuthorizLogRepository.save(userAuthorizLog);
    }

    @Override
    public UserAuthorizLog getById(Long id) {
        return userAuthorizLogRepository.findById(id).orElse(null);
    }

    @Override
    public List<UserAuthorizLog> getAll() {
        List<UserAuthorizLog> authorizLogs = userAuthorizLogRepository.findAll();
        if (authorizLogs.isEmpty())
            throw new IllegalArgumentException("Записей не найдено");
        return authorizLogs;
    }

    @Override
    public Boolean hasThreeFailsLastsLogsByUserId(Long id) {
        return userAuthorizLogRepository.hasThreeFailsInARowByUserId(id);
    }

    @Override
    public UserAuthorizLog getLastLogByUserId(Long id) {
        return userAuthorizLogRepository.findLastLogByUserId(id).orElse(null);
    }

    @Override
    public UserAuthorizLog update(UserAuthorizLog userAuthorizLog) {
        return null;
    }
}