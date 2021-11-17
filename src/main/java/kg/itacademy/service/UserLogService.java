package kg.itacademy.service;

import kg.itacademy.entity.UserLog;
import kg.itacademy.model.UserLogModel;

import java.util.List;

public interface UserLogService extends BaseService<UserLog> {
    boolean hasThreeFailsLastsLogsByUserId(Long id);

    UserLog getLastLogByUserId(Long id);

    List<UserLogModel> getAllByUserId(Long id);
}