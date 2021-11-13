package kg.itacademy.service.impl;

import kg.itacademy.converter.UserConverter;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserAuthorizLog;
import kg.itacademy.entity.UserBalance;
import kg.itacademy.entity.UserRole;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.UserAuthorizModel;
import kg.itacademy.model.UserModel;
import kg.itacademy.repository.UserRepository;
import kg.itacademy.service.UserAuthrizLogService;
import kg.itacademy.service.UserBalanceService;
import kg.itacademy.service.UserRoleService;
import kg.itacademy.service.UserService;
import kg.itacademy.util.VariableValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, VariableValidation<User> {

    private final UserRepository userRepository;

    private final UserRoleService userRoleService;

    private final UserBalanceService userBalanceService;

    private final PasswordEncoder passwordEncoder;

    private final UserAuthrizLogService userAuthrizLogService;

    @Override
    public User save(User user) {
        validateSpace(user);
        validateVariablesForNullOrIsEmpty(user);
        checkUsernameAndEmail(user);
        validateEmail(user.getEmail());
        validateLengthVariables(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(1L);
        userRepository.save(user);

        userRoleService.save(UserRole.builder()
                .roleName("ROLE_USER")
                .user(user)
                .build());

        userBalanceService.save(UserBalance.builder()
                .user(user)
                .balance(new BigDecimal(0))
                .build());

        return user;
    }

    @Override
    public UserModel createUser(User user) {
        save(user);
        return new UserConverter().convertFromEntity(user);
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty())
            throw new IllegalArgumentException("Пользователей не найдено");
        return users;
    }

    @Override
    public List<UserModel> getAllUserModels() {
        List<UserModel> userModels = new ArrayList<>();
        for (User user : getAll())
            userModels.add(new UserConverter().convertFromEntity(user));
        return userModels;
    }

    @Override
    public User getById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            throw new ApiFailException("Не найден пользователь по id: " + id);
        return user;
    }

    @Override
    public UserModel getUserModelById(Long id) {
        return new UserConverter().convertFromEntity(getById(id));
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
    public UserModel getCurrentUserModel() {
        return new UserConverter().convertFromEntity(getCurrentUser());
    }

    @Override
    public User update(User user) {
        if (user.getId() == null)
            throw new IllegalArgumentException("Не указан Id пользователя");
        if (user.getEmail() != null) {
            validateEmail(user.getEmail());
        }
        validateSpaceForUpdate(user);
        validateVariablesForNullOrIsEmptyUpdate(user);
        checkUsernameAndEmailForUpdate(user);
        validateLengthVariablesForUpdateUser(user);

        return userRepository.save(user);
    }

    @Override
    public UserModel updateUser(User user) {
        User updateUser = update(user);
        return new UserConverter()
                .convertFromEntity(update(updateUser));
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
        userAuthrizLogService.save(new UserAuthorizLog(user, true));

        return "Basic " + authHeader;
    }


    @Override
    public User setInActiveUser(User user, Long status) {
        user.setIsActive(status);
        return userRepository.save(user);
    }

    @Override
    public UserModel deleteUser() {
        User user = getCurrentUser();
        User deleteUser = setInActiveUser(user, -1L);
        return new UserConverter().convertFromEntity(deleteUser);
    }

    @Override
    public void validateVariablesForNullOrIsEmpty(User user) {
        if (user.getFullName() == null || user.getFullName().isEmpty())
            throw new ApiFailException("Не заполнен full name");
        if (user.getUsername() == null || user.getUsername().isEmpty())
            throw new ApiFailException("Не заполнен username");
        if (user.getEmail() == null || user.getEmail().isEmpty())
            throw new ApiFailException("Не заполнен email");
        if (user.getPassword() == null || user.getPassword().isEmpty())
            throw new ApiFailException("Не заполнен password");
    }

    @Override
    public void validateVariablesForNullOrIsEmptyUpdate(User user) {
        if (user.getEmail() != null && user.getFullName().isEmpty())
            throw new ApiFailException("full name не может быть пустым");

        if (user.getUsername() != null && user.getUsername().isEmpty())
            throw new ApiFailException("username не может быть пустым");

        if (user.getEmail() != null && user.getEmail().isEmpty())
            throw new ApiFailException("email не может быть пустым");


        if (user.getPassword() != null && user.getPassword().isEmpty())
            throw new ApiFailException("password не может быть пустым");
    }

    private void checkUsernameAndEmail(User user) {
        User dataUserByUserName = getByUsername(user.getUsername());
        User dataUserByEmail = getByEmail(user.getEmail());

        if (dataUserByUserName != null)
            throw new IllegalArgumentException("Такой пользователь " + dataUserByUserName.getUsername() + " уже существует");

        if (dataUserByEmail != null)
            throw new IllegalArgumentException("Электронная почта " + dataUserByEmail.getEmail() + " уже используется");
    }


    private void checkUsernameAndEmailForUpdate(User user) {
        User dataUserByUserName = getByUsername(user.getUsername());
        User dataUserByEmail = getByEmail(user.getEmail());

        if (user.getUsername() != null && dataUserByUserName != null)
            throw new IllegalArgumentException("Такой пользователь " + dataUserByUserName.getUsername() + " уже существует");

        if (user.getEmail() != null && dataUserByEmail != null)
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
            userAuthrizLogService.save(new UserAuthorizLog(user, false));

            boolean needToBan = userAuthrizLogService.hasThreeFailsLastsLogsByUserId(user.getId());

            if (needToBan)
                setInActiveUser(user, 0L);
            throw new IllegalArgumentException("Неверный логин или пароль");
        }
    }

    @Override
    public void validateLengthVariables(User user) {
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

    @Override
    public void validateLengthVariablesForUpdateUser(User user) {
        if (user.getFullName() != null && user.getFullName().length() > 100)
            throw new IllegalArgumentException("Вы превысили лимит(50) символов full name");

        if (user.getUsername() != null && user.getUsername().length() > 50)
            throw new IllegalArgumentException("Вы превысили лимит(50) символов username");

        if (user.getEmail() != null && user.getEmail().length() > 50)
            throw new IllegalArgumentException("Вы превысили лимит(50) символов email");

        if (user.getPassword() != null && user.getPassword().length() > 50)
            throw new IllegalArgumentException("Вы превысили лимит(50) символов password");
        else if (user.getPassword() != null && user.getPassword().length() < 6)
            throw new IllegalArgumentException("Количество символов password должна быть больше 5");
    }

    private void validateEmail(String email) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Matcher matcher = Pattern.compile(emailRegex).matcher(email);
        if (!matcher.matches()) {
            throw new ApiFailException("Не правильный форма email");
        }
    }

    private void validateSpace(User user) {
        if (user.getUsername().contains(" "))
            throw new ApiFailException("Не правильный формат username");
        if (user.getEmail().contains(" "))
            throw new ApiFailException("Не правильный формат email");
    }

    private void validateSpaceForUpdate(User user) {
        if (user.getUsername() != null && user.getUsername().contains(" "))
            throw new ApiFailException("Не правильный формат username");
        if (user.getUsername() != null && user.getEmail().contains(" "))
            throw new ApiFailException("Не правильный формат email");
    }
}