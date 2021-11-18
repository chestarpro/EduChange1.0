package kg.itacademy.service.impl;

import kg.itacademy.converter.UserRoleConverter;
import kg.itacademy.entity.UserRole;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.UserRoleModel;
import kg.itacademy.repository.UserRoleRepository;
import kg.itacademy.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Override
    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole getById(Long id) {
        return userRoleRepository.findById(id).orElse(null);
    }

    @Override
    public List<UserRole> getAll() {
        return userRoleRepository.findAll();
    }

    @Override
    public List<UserRoleModel> getAllUserRoleModel() {
        UserRoleConverter userRoleConverter = new UserRoleConverter();
        return getAll().stream()
                .map(userRoleConverter::convertFromEntity).collect(Collectors.toList());
    }

    @Override
    public UserRole update(UserRole userRole) {
        return null;
    }
}