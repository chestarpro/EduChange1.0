package kg.itacademy.service.impl;

import kg.itacademy.converter.UserRoleConverter;
import kg.itacademy.entity.UserRole;
import kg.itacademy.repository.UserRoleRepository;
import kg.itacademy.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository USER_ROLE_REPOSITORY;
    private final UserRoleConverter USER_ROLE_CONVERTER;

    @Override
    public UserRole save(UserRole userRole) {
        return USER_ROLE_REPOSITORY.save(userRole);
    }

    @Override
    public UserRole getById(Long id) {
        return USER_ROLE_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public List<UserRole> getAll() {
        return USER_ROLE_REPOSITORY.findAll();
    }
}