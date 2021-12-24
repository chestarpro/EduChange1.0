package kg.itacademy.service.impl;

import kg.itacademy.converter.UserConverter;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserLog;
import kg.itacademy.entity.UserBalance;
import kg.itacademy.entity.UserRole;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.course.CourseDataModel;
import kg.itacademy.model.user.*;
import kg.itacademy.repository.UserRepository;
import kg.itacademy.service.*;
import kg.itacademy.util.RegexUtil;
import kg.itacademy.model.userImage.ResetPasswordModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserImageService userImageService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserBalanceService userBalanceService;
    @Autowired
    private UserCourseMappingService userCourseMappingService;
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final UserLogService userLogService;
    private final UserConverter converter;

    @Override
    public User save(User user) {
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
    public UserProfileDataModel createUser(CreateUserModel createUserModel) {
        validateVariablesForNullOrIsEmpty(createUserModel);
        validateLengthVariables(createUserModel);
        RegexUtil.validateUsername(createUserModel.getUsername());
        RegexUtil.validateEmail(createUserModel.getEmail());
        checkUsernameAndEmail(createUserModel);

        String token = getBasicToken(createUserModel.getUsername(), createUserModel.getPassword());

        User dataUser = converter.convertFromModel(createUserModel);
        save(dataUser);
        return getUserProfileDataModelByUserId(token, dataUser.getId());
    }

    @Override
    public UserProfileDataModel getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel) {
        User user = userRepository.findByUsername(userAuthorizModel.getUsername())
                .orElseThrow(() -> new ApiFailException("Неправильное имя пользователя или пароль"));

        boolean isPasswordIsCorrect = passwordEncoder.matches(userAuthorizModel.getPassword(), user.getPassword());

        checkUserActiveStatus(user);
        checkFailPassword(isPasswordIsCorrect, user);

        userLogService.save(new UserLog(user, true));
        String token = getBasicToken(userAuthorizModel.getUsername(), userAuthorizModel.getPassword());
        return getUserProfileDataModelByUserId(token, user.getId());
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<BaseUserModel> getAllUserModels() {
        return getAll().stream()
                .map(converter::convertFromEntity).collect(Collectors.toList());
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public BaseUserModel getUserModelById(Long id) {
        return converter.convertFromEntity(getById(id));
    }

    @Override
    public UserProfileDataModel getCurrentUserProfileDataModel() {
        Long currentUserId = getCurrentUser().getId();
        return getUserProfileDataModelByUserId(null, currentUserId);
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
    public BaseUserModel getCurrentUserModel() {
        return converter.convertFromEntity(getCurrentUser());
    }

    @Override
    public UserProfileDataModel updateUser(UpdateUserModel updateUserModel) {
        User dataUser = getDataUserWithCheckAccess(updateUserModel.getId());

        validateVariablesForNullOrIsEmptyUpdate(updateUserModel);
        validateLengthVariables(updateUserModel);
        RegexUtil.validateEmail(updateUserModel.getEmail());
        RegexUtil.validateUsername(updateUserModel.getUsername());
        checkUsernameAndEmail(updateUserModel);

        setVariablesForUpdateUser(dataUser, updateUserModel);
        dataUser = userRepository.save(dataUser);

        String token = null;
        if (updateUserModel.getPassword() != null)
            token = getBasicToken(dataUser.getUsername(), updateUserModel.getPassword());
        return getUserProfileDataModelByUserId(token, dataUser.getId());
    }

    @Override
    public BaseUserModel resetPassword(ResetPasswordModel resetPasswordModel) {
        String email = new String(Base64.getDecoder().decode(resetPasswordModel.getEncodeEmail().getBytes()));
        User user = getByEmail(email);

        String newPassword = resetPasswordModel.getPassword();
        if (newPassword == null || newPassword.isEmpty())
            throw new ApiFailException("Password не заполнен");
        if (newPassword.length() < 6)
            throw new ApiFailException("Количество символов пароля должно быть более 5");

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return converter.convertFromEntity(user);
    }

    @Override
    public User setInActiveUser(User user, Long status) {
        user.setIsActive(status);
        return userRepository.save(user);
    }

    @Override
    public BaseUserModel deleteUser() {
        User user = getCurrentUser();
        User deleteUser = setInActiveUser(user, -1L);
        return converter.convertFromEntity(deleteUser);
    }

    @Override
    public BaseUserModel deleteUserByAdmin(Long userId) {
        User user = getById(userId);
        User deleteUser = setInActiveUser(user, -1L);
        return converter.convertFromEntity(deleteUser);
    }

    private User getDataUserWithCheckAccess(Long userId) {
        if (userId == null)
            throw new ApiFailException("Не указан id пользователя");

        User dataUser = getById(userId);
        if (dataUser == null)
            throw new ApiFailException("Пользователь под ID " + userId + " не найден");

        if (!userId.equals(getCurrentUser().getId()))
            throw new ApiFailException("Доступ ограничен");

        return dataUser;
    }

    private String getBasicToken(String username, String password) {
        String usernamePasswordPair = username + ":" + password;
        String authHeader = new String(Base64.getEncoder().encode(usernamePasswordPair.getBytes()));
        return "Basic " + authHeader;
    }

    private void validateVariablesForNullOrIsEmpty(CreateUserModel createUserModel) {
        if (createUserModel.getFullName() == null || createUserModel.getFullName().isEmpty())
            throw new ApiFailException("Full name не заполнен");
        if (createUserModel.getUsername() == null || createUserModel.getUsername().isEmpty())
            throw new ApiFailException("Username не заполнен");
        if (createUserModel.getEmail() == null || createUserModel.getEmail().isEmpty())
            throw new ApiFailException("Email не заполнен");
        if (createUserModel.getPassword() == null || createUserModel.getPassword().isEmpty())
            throw new ApiFailException("Password не заполнен");
    }

    private void validateVariablesForNullOrIsEmptyUpdate(UpdateUserModel userModel) {
        if (userModel.getEmail() != null && userModel.getFullName().isEmpty())
            throw new ApiFailException("Full name не заполнен");
        if (userModel.getUsername() != null && userModel.getUsername().isEmpty())
            throw new ApiFailException("Username не заполнен");
        if (userModel.getEmail() != null && userModel.getEmail().isEmpty())
            throw new ApiFailException("Email is не заполнен");
        if (userModel.getPassword() != null && userModel.getPassword().isEmpty())
            throw new ApiFailException("Password не заполнен");
    }

    private void validateLengthVariables(BaseUserModel baseUserModel) {
        if (baseUserModel.getFullName() != null && baseUserModel.getFullName().length() > 100)
            throw new ApiFailException("Длинна символов full name ограниченно(100)");
        if (baseUserModel.getUsername() != null && baseUserModel.getUsername().length() > 100)
            throw new ApiFailException("Длинна символов username ограниченно(100)");
        if (baseUserModel.getEmail() != null && baseUserModel.getEmail().length() > 100)
            throw new ApiFailException("Длинна символов email ограниченно(100)");
        if (baseUserModel.getPassword() != null && baseUserModel.getPassword().length() > 100)
            throw new ApiFailException("Длинна символов password ограниченно(100)");
        else if (baseUserModel.getPassword() != null && baseUserModel.getPassword().length() < 6)
            throw new ApiFailException("Количество символов пароля должно быть более 5");
    }

    private void checkUsernameAndEmail(BaseUserModel baseUserModel) {
        User dataUserByUserName = getByUsername(baseUserModel.getUsername());
        User dataUserByEmail = getByEmail(baseUserModel.getEmail());

        if (dataUserByUserName != null)
            throw new ApiFailException("Username " + dataUserByUserName.getUsername() + " уже существует");

        if (dataUserByEmail != null)
            throw new ApiFailException("Email " + dataUserByEmail.getEmail() + " уже используется");
    }

    private void checkUserActiveStatus(User user) {
        if (user.getIsActive() == -1)
            throw new ApiFailException("Пользователь не найден");
        if (user.getIsActive() == 0) {
            UserLog userLog = userLogService.getLastLogByUserId(user.getId());
            if (LocalDateTime.now().isAfter(userLog.getCreateDate().plusMinutes(5)))
                setInActiveUser(user, 1L);
            else throw new ApiFailException("Вы были заблокированный на 5 минут");
        }
    }

    private void checkFailPassword(boolean isPasswordIsCorrect, User user) {
        if (!isPasswordIsCorrect) {
            userLogService.save(new UserLog(user, false));

            boolean needToBan = userLogService.hasThreeFailsLastsLogsByUserId(user.getId());

            if (needToBan)
                setInActiveUser(user, 0L);

            throw new ApiFailException("Неправильное имя пользователя или пароль");
        }
    }

    private UserProfileDataModel getUserProfileDataModelByUserId(String token, Long userId) {
        UserProfileDataModel dataBaseModel = new UserProfileDataModel();
        dataBaseModel.setToken(token);
        List<CourseDataModel> userCreateCourses = courseService.getAllCourseDataModelByUserId(userId);
        List<CourseDataModel> userPurchasedCourses = userCourseMappingService.getAllPurchasedCourses(userId);
        dataBaseModel.setUserModelToSend((UserModelToSend) getUserModelById(userId));
        dataBaseModel.setUserBalanceModel(userBalanceService.getUserBalanceModelByUserId(userId));
        dataBaseModel.setUserImageModel(userImageService.getUserImageModelByUserId(userId));
        dataBaseModel.setUserCreateCourseModels(userCreateCourses);
        dataBaseModel.setUserPurchasedCourseModels(userPurchasedCourses);
        return dataBaseModel;
    }

    private void setVariablesForUpdateUser(User user, UpdateUserModel updateUserModel) {
        if (updateUserModel.getUsername() != null)
            user.setUsername(updateUserModel.getUsername());
        if (updateUserModel.getFullName() != null)
            user.setFullName(updateUserModel.getFullName());
        if (updateUserModel.getEmail() != null)
            user.setEmail(updateUserModel.getEmail());
        if (updateUserModel.getPassword() != null)
            user.setPassword(passwordEncoder.encode(updateUserModel.getPassword()));
        if (updateUserModel.getBirthDay() != null)
            user.setBirthDay(updateUserModel.getBirthDay());
    }
}