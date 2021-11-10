package kg.itacademy.service.impl;

import kg.itacademy.entity.User;
import kg.itacademy.entity.UserAuthorizLog;
import kg.itacademy.entity.UserBalance;
import kg.itacademy.entity.UserRole;
import kg.itacademy.model.UserAuthorizModel;
import kg.itacademy.repository.UserRepository;
import kg.itacademy.service.UserAuthrizLogService;
import kg.itacademy.service.UserBalanceService;
import kg.itacademy.service.UserRoleService;
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

    private final UserRoleService userRoleService;

    private final UserBalanceService userBalanceService;

    private final PasswordEncoder passwordEncoder;

    private final UserAuthrizLogService userAuthrizLogService;

    @Override
    public User create(User user) {
        checkForVariables(user);
        checkCorrectLengthVariables(user);
        checkUsernameAndEmail(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(1L);
        userRepository.save(user);

        userRoleService.create(UserRole.builder()
                .roleName("ROLE_USER")
                .user(user)
                .build());

        userBalanceService.create(UserBalance.builder()
                .user(user)
                .balance(new BigDecimal(0))
                .build());

        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty())
            throw new IllegalArgumentException("Пользователей не найдено");
        return users;
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
        if (user.getId() == null)
            throw new IllegalArgumentException("Не указан Id пользователя");

        checkCorrectLengthVariablesFotUpdateUser(user);

        return userRepository.save(user);
    }

    @Override
    public String getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel) {
        User user = userRepository.findByUsername(userAuthorizModel.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Не верный логин или пароль"));

        boolean isPasswordIsCorrect = passwordEncoder.matches(userAuthorizModel.getPassword(), user.getPassword());

        checkBaningStatus(user);

        checkFailPassword(isPasswordIsCorrect, user);

        String usernamePasswordPair = userAuthorizModel.getUsername() + ":" + userAuthorizModel.getPassword();
        String authHeader = new String(Base64.getEncoder().encode(usernamePasswordPair.getBytes()));
        userAuthrizLogService.create(new UserAuthorizLog(user, true));

        return "Basic " + authHeader;
    }

    @Override
    public User setInActiveUser(User user, Long status) {
        user.setIsActive(status);
        return update(user);
    }

    private void checkForVariables(User user) {
        if (user.getFullName() == null)
            throw new IllegalArgumentException("Не заполнен full name");
        if (user.getUsername() == null)
            throw new IllegalArgumentException("Не заполнен username");
        if (user.getEmail() == null)
            throw new IllegalArgumentException("Не заполнен email");
        if (user.getPassword() == null)
            throw new IllegalArgumentException("Не заполнен password");
    }

    private void checkUsernameAndEmail(User user) {
        User dataUserByUserName = getByUsername(user.getUsername());
        User dataUserByEmail = getByEmail(user.getEmail());

        if (dataUserByUserName != null)
            throw new IllegalArgumentException("Такой пользователь " + dataUserByUserName.getUsername() + " уже существует");

        if (dataUserByEmail != null)
            throw new IllegalArgumentException("Электронная почта " + dataUserByEmail.getEmail() + " уже используется");
    }

    private void checkBaningStatus(User user) {
        if (user.getIsActive() == 0) {
            UserAuthorizLog userAuthorizLog = userAuthrizLogService.getLastLogByUserId(user.getId());
            if (LocalDateTime.now().isAfter(userAuthorizLog.getCreateDate().plusMinutes(5)))
                setInActiveUser(user, 1L);
            else throw new IllegalArgumentException("Вы за банены на 5 мин");
        }
    }

    private void checkFailPassword(boolean isPasswordIsCorrect, User user) {
        if (!isPasswordIsCorrect) {
            userAuthrizLogService.create(new UserAuthorizLog(user, false));

            boolean needToBan = userAuthrizLogService.hasThreeFailsLastsLogsByUserId(user.getId());

            if (needToBan)
                setInActiveUser(user, 0L);
            throw new IllegalArgumentException("Неверный логин или пароль");
        }
    }

    private void checkCorrectLengthVariables(User user) {
        if (user.getFullName().length() > 100)
            throw new IllegalArgumentException("Вы превысили лимит(50) символов full name");
        if (user.getUsername().length() > 50)
            throw new IllegalArgumentException("Вы превысили лимит(50) символов username");
        if (user.getEmail().length() > 50)
            throw new IllegalArgumentException("Вы превысили лимит(50) символов email");
        if (user.getPassword().length() > 50)
            throw new IllegalArgumentException("Вы превысили лимит(50) символов password");
        if (user.getPassword().length() < 6)
            throw new IllegalArgumentException("Количество символов password должна быть больше 5");
    }

    private void checkCorrectLengthVariablesFotUpdateUser(User user) {
        if (user.getFullName() != null) {
            if (user.getFullName().length() > 100)
                throw new IllegalArgumentException("Вы превысили лимит(50) символов full name");
        }
        if (user.getUsername() != null) {
            if (user.getUsername().length() > 50)
                throw new IllegalArgumentException("Вы превысили лимит(50) символов username");
        }
        if (user.getEmail() != null) {
            if (user.getEmail().length() > 50)
                throw new IllegalArgumentException("Вы превысили лимит(50) символов email");
        }
        if (user.getPassword() != null) {
            if (user.getPassword().length() > 50)
                throw new IllegalArgumentException("Вы превысили лимит(50) символов password");
            else if (user.getPassword().length() < 6)
                throw new IllegalArgumentException("Количество символов password должна быть больше 5");
        }
    }
}