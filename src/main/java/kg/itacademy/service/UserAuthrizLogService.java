package kg.itacademy.service;

import kg.itacademy.entity.UserAuthorizLog;

public interface UserAuthrizLogService extends BaseService<UserAuthorizLog> {
    Boolean hasThreeFailsLastsLogsByUserId(Long id);

    UserAuthorizLog getLastLogByUserId(Long id);
}
