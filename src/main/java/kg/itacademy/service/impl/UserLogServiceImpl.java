package kg.itacademy.service.impl;

import kg.itacademy.converter.UserLogConverter;
import kg.itacademy.entity.UserLog;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.UserLogModel;
import kg.itacademy.repository.UserLogRepository;
import kg.itacademy.service.UserLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLogServiceImpl implements UserLogService {

    private final UserLogRepository userLogRepository;

    @Override
    public UserLog save(UserLog userLog) {
        return userLogRepository.save(userLog);
    }

    @Override
    public UserLog getById(Long id) {
        return userLogRepository.findById(id).orElse(null);
    }

    @Override
    public List<UserLog> getAll() {
        return userLogRepository.findAll();
    }

    @Override
    public List<UserLogModel> getAllByUserId(Long id) {
        List<UserLog> logList = userLogRepository.findAllByUser_Id(id);
        if (logList == null) {
            throw new ApiFailException("Записи по пользователю (id: " + id + ") не найдены");
        }
        List<UserLogModel> logModelList = new ArrayList<>();
        for (UserLog userLog : logList)
            logModelList.add(new UserLogConverter().convertFromEntity(userLog));
        return logModelList;
    }

    @Override
    public Boolean hasThreeFailsLastsLogsByUserId(Long id) {
        return userLogRepository.hasThreeFailsInARowByUserId(id);
    }

    @Override
    public UserLog getLastLogByUserId(Long id) {
        return userLogRepository.findLastLogByUserId(id).orElse(null);
    }

    @Override
    public UserLog update(UserLog userLog) {
        return null;
    }
}