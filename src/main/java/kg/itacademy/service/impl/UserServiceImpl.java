package kg.itacademy.service.impl;

import kg.itacademy.entity.User;
import kg.itacademy.entity.UserAuthorizLog;
import kg.itacademy.entity.UserBalance;
import kg.itacademy.entity.UserRole;
import kg.itacademy.model.UserAuthorizModel;
import kg.itacademy.repository.UserAuthorizLogRepository;
import kg.itacademy.repository.UserRepository;
import kg.itacademy.repository.UserRoleRepository;
import kg.itacademy.service.UserBalanceService;
import kg.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    private final UserRoleRepository userRoleRepository;


    private final UserBalanceService userBalanceService;


    private final PasswordEncoder passwordEncoder;


    private final UserAuthorizLogRepository userAuthorizLogRepository;

    @Override
    public User create(User user) {
        User dataUserByUserName = getByUsername(user.getUsername());
        User dataUserByEmail = getByUsername(user.getUsername());

        if (dataUserByUserName != null)
            throw new IllegalArgumentException("Такой пользователь " + dataUserByUserName.getUsername() + " уже существует");

        if (dataUserByEmail != null)
            throw new IllegalArgumentException("Электронная почта " + dataUserByEmail.getEmail() + " уже используется");

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setIsActive(1L);
        userRepository.save(user);

        UserRole userRole = new UserRole();
        userRole.setRoleName("ROLE_USER");
        userRole.setUser(user);
        userRoleRepository.save(userRole);

        UserBalance userBalance = new UserBalance();
        userBalance.setUser(user);
        userBalance.setBalance(new BigDecimal(0));
        userBalanceService.create(userBalance);

        return user;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User getCurrentUser() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(login);
    }

    @Override
    public User update(User user) {

        return userRepository.save(user);
    }

    @Override
    public String getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel) {
        User user = userRepository.findByUsername(userAuthorizModel.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Не верный логин или пароль"));

        boolean isPasswordIsCorrect = passwordEncoder.matches(userAuthorizModel.getPassword(), user.getPassword());

        if(user.getIsActive() == 0) {
            throw new IllegalArgumentException("Вы забанены");
        }
        if (!isPasswordIsCorrect) {
            userAuthorizLogRepository.save(new UserAuthorizLog(user, false));

            boolean needToBan = userAuthorizLogRepository.hasThreeFailsInARowByUserId(user.getId());
            if(needToBan) {
                user.setIsActive(0L);
                userRepository.save(user);
            }
            throw new IllegalArgumentException("Неверный логин или пароль");
        }
        String usernamePasswordPair = userAuthorizModel.getUsername() + ":" + userAuthorizModel.getPassword();
        String authHeader = new String(Base64.getEncoder().encode(usernamePasswordPair.getBytes()));
        userAuthorizLogRepository.save(new UserAuthorizLog(user, true));


        return "Basic " + authHeader;
    }

    @Override
    public User setInActiveUser() {
        User user = getCurrentUser();
        user.setIsActive(0L);
        return update(user);
    }
}