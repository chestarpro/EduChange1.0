package kg.itacademy.service.impl;

import kg.itacademy.converter.UserLogConverter;
import kg.itacademy.entity.UserLog;
import kg.itacademy.model.UserLogModel;
import kg.itacademy.repository.UserLogRepository;
import kg.itacademy.service.UserLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLogServiceImpl implements UserLogService {
    private final UserLogRepository USER_LOG_REPOSITORY;
    private final UserLogConverter USER_LOG_CONVERTER;

    @Override
    public UserLog save(UserLog userLog) {
        return USER_LOG_REPOSITORY.save(userLog);
    }

    @Override
    public UserLog getById(Long id) {
        return USER_LOG_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public List<UserLog> getAll() {
        return USER_LOG_REPOSITORY.findAll();
    }

    @Override
    public List<UserLogModel> getAllByUserId(Long id) {
        return USER_LOG_REPOSITORY.findAllByUser_Id(id).stream()
                .map(USER_LOG_CONVERTER::convertFromEntity).collect(Collectors.toList());
    }

    @Override
    public boolean hasThreeFailsLastsLogsByUserId(Long id) {
        return USER_LOG_REPOSITORY.hasThreeFailsInARowByUserId(id);
    }

    @Override
    public UserLog getLastLogByUserId(Long id) {
        return USER_LOG_REPOSITORY.findLastLogByUserId(id).orElse(null);
    }
}