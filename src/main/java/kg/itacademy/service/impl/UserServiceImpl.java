package kg.itacademy.service.impl;

import kg.itacademy.converter.UserConverter;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserLog;
import kg.itacademy.entity.UserBalance;
import kg.itacademy.entity.UserRole;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.UserAuthorizModel;
import kg.itacademy.model.UserModel;
import kg.itacademy.repository.UserRepository;
import kg.itacademy.service.UserLogService;
import kg.itacademy.service.UserBalanceService;
import kg.itacademy.service.UserRoleService;
import kg.itacademy.service.UserService;
import kg.itacademy.util.VariableValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, VariableValidation<User> {

    private final UserRepository userRepository;

    private final UserRoleService userRoleService;

    private final PasswordEncoder passwordEncoder;

    private final UserLogService userLogService;

    @Autowired
    private UserBalanceService userBalanceService;

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
        return new UserConverter().convertFromEntity(save(user));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
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
            throw new ApiFailException("Не указан Id пользователя");
        if (user.getEmail() != null) {
            validateEmail(user.getEmail());
        }
        validateSpaceForUpdate(user);
        validateVariablesForNullOrIsEmptyUpdate(user);
        checkUsernameAndEmailForUpdate(user);
        validateLengthVariablesForUpdate(user);

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
                .orElseThrow(() -> new ApiFailException("Не верный логин или пароль"));

        boolean isPasswordIsCorrect = passwordEncoder.matches(userAuthorizModel.getPassword(), user.getPassword());

        checkBaningStatus(user);
        checkFailPassword(isPasswordIsCorrect, user);

        String usernamePasswordPair = userAuthorizModel.getUsername() + ":" + userAuthorizModel.getPassword();
        String authHeader = new String(Base64.getEncoder().encode(usernamePasswordPair.getBytes()));
        userLogService.save(new UserLog(user, true));

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
            throw new ApiFailException("Такой пользователь " + dataUserByUserName.getUsername() + " уже существует");

        if (dataUserByEmail != null)
            throw new ApiFailException("Электронная почта " + dataUserByEmail.getEmail() + " уже используется");
    }


    private void checkUsernameAndEmailForUpdate(User user) {
        User dataUserByUserName = getByUsername(user.getUsername());
        User dataUserByEmail = getByEmail(user.getEmail());

        if (user.getUsername() != null && dataUserByUserName != null)
            throw new ApiFailException("Такой пользователь " + dataUserByUserName.getUsername() + " уже существует");

        if (user.getEmail() != null && dataUserByEmail != null)
            throw new ApiFailException("Электронная почта " + dataUserByEmail.getEmail() + " уже используется");
    }

    private void checkBaningStatus(User user) {
        if (user.getIsActive() == 0) {
            UserLog userLog = userLogService.getLastLogByUserId(user.getId());
            if (LocalDateTime.now().isAfter(userLog.getCreateDate().plusMinutes(5)))
                setInActiveUser(user, 1L);
            else throw new ApiFailException("Вы за банены на 5 мин");
        }
    }

    private void checkFailPassword(boolean isPasswordIsCorrect, User user) {
        if (!isPasswordIsCorrect) {
            userLogService.save(new UserLog(user, false));

            boolean needToBan = userLogService.hasThreeFailsLastsLogsByUserId(user.getId());

            if (needToBan)
                setInActiveUser(user, 0L);
            throw new ApiFailException("Неверный логин или пароль");
        }
    }

    @Override
    public void validateLengthVariables(User user) {
        if (user.getFullName().length() > 100)
            throw new ApiFailException("Вы превысили лимит(50) символов full name");

        if (user.getUsername().length() > 50)

            throw new ApiFailException("Вы превысили лимит(50) символов username");
        if (user.getEmail().length() > 50)
            throw new ApiFailException("Вы превысили лимит(50) символов email");

        if (user.getPassword().length() > 50)
            throw new ApiFailException("Вы превысили лимит(50) символов password");

        if (user.getPassword().length() < 6)
            throw new ApiFailException("Количество символов password должна быть больше 5");
    }

    @Override
    public void validateLengthVariablesForUpdate(User user) {
        if (user.getFullName() != null && user.getFullName().length() > 100)
            throw new ApiFailException("Вы превысили лимит(50) символов full name");

        if (user.getUsername() != null && user.getUsername().length() > 50)
            throw new ApiFailException("Вы превысили лимит(50) символов username");

        if (user.getEmail() != null && user.getEmail().length() > 50)
            throw new ApiFailException("Вы превысили лимит(50) символов email");

        if (user.getPassword() != null && user.getPassword().length() > 50)
            throw new ApiFailException("Вы превысили лимит(50) символов password");

        else if (user.getPassword() != null && user.getPassword().length() < 6)
            throw new ApiFailException("Количество символов password должна быть больше 5");
    }

    private void validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
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