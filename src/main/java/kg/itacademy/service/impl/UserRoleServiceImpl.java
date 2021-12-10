package kg.itacademy.service.impl;

import kg.itacademy.converter.UserRoleConverter;
import kg.itacademy.entity.UserRole;
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

    private final UserRoleConverter CONVERTER = new UserRoleConverter();

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

        return getAll().stream()
                .map(CONVERTER::convertFromEntity).collect(Collectors.toList());
    }
}