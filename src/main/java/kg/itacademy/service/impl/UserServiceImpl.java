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
    private UserImageService USER_IMAGE_SERVICE;
    @Autowired
    private CourseService COURSE_SERVICE;
    @Autowired
    private UserBalanceService USER_BALANCE_SERVICE;
    @Autowired
    private UserCourseMappingService USER_COURSE_MAPPING_SERVICE;
    private final UserRepository USER_REPOSITORY;
    private final UserRoleService USER_ROLE_SERVICE;
    private final PasswordEncoder PASSWORD_ENCODER;
    private final UserLogService USER_LOG_SERVICE;
    private final UserConverter USER_CONVERTER;
    private final RegexUtil REGEX_UTIL;

    @Override
    public User save(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setIsActive(1L);
        USER_REPOSITORY.save(user);

        USER_ROLE_SERVICE.save(UserRole.builder()
                .roleName("ROLE_USER")
                .user(user)
                .build());

        USER_BALANCE_SERVICE.save(UserBalance.builder()
                .user(user)
                .balance(new BigDecimal(0))
                .build());

        return user;
    }

    @Override
    public UserProfileDataModel createUser(CreateUserModel createUserModel) {
        validateVariablesForNullOrIsEmpty(createUserModel);
        validateLengthVariables(createUserModel);
        validateSpace(createUserModel);
        REGEX_UTIL.validateUsername(createUserModel.getUsername());
        REGEX_UTIL.validateEmail(createUserModel.getEmail());
        checkUsernameAndEmail(createUserModel);

        String token = getToken(createUserModel.getUsername(), createUserModel.getPassword());

        User dataUser = save(USER_CONVERTER.convertFromModel(createUserModel));
        return getUserProfileDataModelByUserId(token, dataUser.getId());
    }

    @Override
    public List<User> getAll() {
        return USER_REPOSITORY.findAll();
    }

    @Override
    public List<BaseUserModel> getAllUserModels() {
        return getAll().stream()
                .map(USER_CONVERTER::convertFromEntity).collect(Collectors.toList());
    }

    @Override
    public User getById(Long id) {
        return USER_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public BaseUserModel getUserModelById(Long id) {
        return USER_CONVERTER.convertFromEntity(getById(id));
    }

    @Override
    public User getByUsername(String username) {
        return USER_REPOSITORY.findByUsername(username).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return USER_REPOSITORY.findByEmail(email).orElse(null);
    }

    @Override
    public User getCurrentUser() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(login);
    }

    @Override
    public BaseUserModel getCurrentUserModel() {
        return USER_CONVERTER.convertFromEntity(getCurrentUser());
    }

    @Override
    public UserProfileDataModel updateUser(UpdateUserModel updateUserModel) {
        Long userId = updateUserModel.getId();

        if (userId == null)
            throw new ApiFailException("User id not specified");

        User dataUser = getById(userId);

        if (dataUser == null)
            throw new ApiFailException("User by id " + userId + " not found");

        if (!userId.equals(getCurrentUser().getId()))
            throw new ApiFailException("Access is denied");

        validateVariablesForNullOrIsEmptyUpdate(updateUserModel);
        validateLengthVariablesForUpdate(updateUserModel);
        validateSpaceForUpdate(updateUserModel);
        REGEX_UTIL.validateEmail(updateUserModel.getEmail());
        REGEX_UTIL.validateUsername(updateUserModel.getUsername());
        checkUsernameAndEmailForUpdate(updateUserModel);

        setForUpdateUser(dataUser, updateUserModel);
        USER_REPOSITORY.save(dataUser);
        return getUserProfileDataModelByUserId(null, userId);
    }

    @Override
    public UserProfileDataModel resetPassword(ResetPasswordModel resetPasswordModel) {
        String email = new String(Base64.getDecoder().decode(resetPasswordModel.getEncodeEmail().getBytes()));
        User user = getByEmail(email);
        if (user == null)
            throw new ApiFailException("Access is denied");

        String newPassword = resetPasswordModel.getPassword();

        if (newPassword == null || newPassword.isEmpty())
            throw new ApiFailException("Password is not filled");
        if (newPassword.length() < 6)
            throw new ApiFailException("The number of password characters must be more than 5");

        user.setPassword(PASSWORD_ENCODER.encode(newPassword));
        USER_REPOSITORY.save(user);
        String token = getToken(user.getUsername(), newPassword);
        return getUserProfileDataModelByUserId(token, user.getId());
    }

    @Override
    public UserProfileDataModel getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel) {
        User user = USER_REPOSITORY.findByUsername(userAuthorizModel.getUsername())
                .orElseThrow(() -> new ApiFailException("Invalid username or password"));

        boolean isPasswordIsCorrect = PASSWORD_ENCODER.matches(userAuthorizModel.getPassword(), user.getPassword());

        checkBaningStatus(user);
        checkFailPassword(isPasswordIsCorrect, user);

        USER_LOG_SERVICE.save(new UserLog(user, true));

        String token = getToken(userAuthorizModel.getUsername(), userAuthorizModel.getPassword());
        return getUserProfileDataModelByUserId(token, user.getId());
    }

    @Override
    public User setInActiveUser(User user, Long status) {
        user.setIsActive(status);
        return USER_REPOSITORY.save(user);
    }

    @Override
    public BaseUserModel deleteUser() {
        User user = getCurrentUser();
        User deleteUser = setInActiveUser(user, -1L);
        return USER_CONVERTER.convertFromEntity(deleteUser);
    }

    @Override
    public BaseUserModel deleteUserByAdmin(Long userId) {
        User user = getById(userId);
        User deleteUser = setInActiveUser(user, -1L);
        return USER_CONVERTER.convertFromEntity(deleteUser);
    }

    private String getToken(String username, String password) {
        String usernamePasswordPair = username + ":" + password;
        String authHeader = new String(Base64.getEncoder().encode(usernamePasswordPair.getBytes()));
        return "Basic " + authHeader;
    }

    private void validateVariablesForNullOrIsEmpty(CreateUserModel createUserModel) {
        if (createUserModel.getFullName() == null || createUserModel.getFullName().isEmpty())
            throw new ApiFailException("Full name is not filled");
        if (createUserModel.getUsername() == null || createUserModel.getUsername().isEmpty())
            throw new ApiFailException("Username is not filled");
        if (createUserModel.getEmail() == null || createUserModel.getEmail().isEmpty())
            throw new ApiFailException("Email is not filled");
        if (createUserModel.getPassword() == null || createUserModel.getPassword().isEmpty())
            throw new ApiFailException("Password is not filled");
    }

    private void validateVariablesForNullOrIsEmptyUpdate(UpdateUserModel userModel) {
        if (userModel.getEmail() != null && userModel.getFullName().isEmpty())
            throw new ApiFailException("Full name is not filled");
        if (userModel.getUsername() != null && userModel.getUsername().isEmpty())
            throw new ApiFailException("Username is not filled");
        if (userModel.getEmail() != null && userModel.getEmail().isEmpty())
            throw new ApiFailException("Email is not filled");
        if (userModel.getPassword() != null && userModel.getPassword().isEmpty())
            throw new ApiFailException("Password is not filled");
    }

    private void validateLengthVariables(CreateUserModel createUserModel) {
        if (createUserModel.getFullName().length() > 100)
            throw new ApiFailException("Exceeded character limit (100) for full name");
        if (createUserModel.getUsername().length() > 100)
            throw new ApiFailException("Exceeded character limit (50) for username");
        if (createUserModel.getEmail().length() > 100)
            throw new ApiFailException("Exceeded character limit (50) for email");
        if (createUserModel.getPassword().length() > 100)
            throw new ApiFailException("Exceeded character limit (50) for password");
        if (createUserModel.getPassword().length() < 6)
            throw new ApiFailException("The number of password characters must be more than 5");
    }

    private void validateLengthVariablesForUpdate(UpdateUserModel updateUserModel) {
        if (updateUserModel.getFullName() != null && updateUserModel.getFullName().length() > 100)
            throw new ApiFailException("Exceeded character limit (100) for full name");
        if (updateUserModel.getUsername() != null && updateUserModel.getUsername().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for username");
        if (updateUserModel.getEmail() != null && updateUserModel.getEmail().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for email");
        if (updateUserModel.getPassword() != null && updateUserModel.getPassword().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for password");
        else if (updateUserModel.getPassword() != null && updateUserModel.getPassword().length() < 6)
            throw new ApiFailException("The number of password characters must be more than 5");
    }

    private void checkUsernameAndEmail(CreateUserModel createUserModel) {
        User dataUserByUserName = getByUsername(createUserModel.getUsername());
        User dataUserByEmail = getByEmail(createUserModel.getEmail());

        if (dataUserByUserName != null)
            throw new ApiFailException("Such user " + dataUserByUserName.getUsername() + " already exists");

        if (dataUserByEmail != null)
            throw new ApiFailException("Email " + dataUserByEmail.getEmail() + " is already in use");
    }

    private void checkUsernameAndEmailForUpdate(UpdateUserModel updateUserModel) {
        User dataUserByUserName = getByUsername(updateUserModel.getUsername());
        User dataUserByEmail = getByEmail(updateUserModel.getEmail());

        if (updateUserModel.getUsername() != null && dataUserByUserName != null)
            throw new ApiFailException("Such user " + dataUserByUserName.getUsername() + " already exists");

        if (updateUserModel.getEmail() != null && dataUserByEmail != null)
            throw new ApiFailException("Email " + dataUserByEmail.getEmail() + " is already in use");
    }

    private void checkBaningStatus(User user) {
        if (user.getIsActive() == 0) {
            UserLog userLog = USER_LOG_SERVICE.getLastLogByUserId(user.getId());
            if (LocalDateTime.now().isAfter(userLog.getCreateDate().plusMinutes(1)))
                setInActiveUser(user, 1L);
            else throw new ApiFailException("You are blocked for 5 minutes");
        }
    }

    private void checkFailPassword(boolean isPasswordIsCorrect, User user) {
        if (!isPasswordIsCorrect) {
            USER_LOG_SERVICE.save(new UserLog(user, false));

            boolean needToBan = USER_LOG_SERVICE.hasThreeFailsLastsLogsByUserId(user.getId());

            if (needToBan)
                setInActiveUser(user, 0L);

            throw new ApiFailException("Invalid username or password");
        }
    }

    private void validateSpace(CreateUserModel createUserModel) {
        if (createUserModel.getUsername().contains(" "))
            throw new ApiFailException("Invalid username format");
        if (createUserModel.getEmail().contains(" "))
            throw new ApiFailException("Invalid email format");
    }

    private void validateSpaceForUpdate(UpdateUserModel updateUserModel) {
        if (updateUserModel.getUsername() != null && updateUserModel.getUsername().contains(" "))
            throw new ApiFailException("Invalid username format");
        if (updateUserModel.getUsername() != null && updateUserModel.getEmail().contains(" "))
            throw new ApiFailException("Invalid email format");
    }

    private UserProfileDataModel getUserProfileDataModelByUserId(String token, Long userId) {
        UserProfileDataModel dataBaseModel = new UserProfileDataModel();
        dataBaseModel.setToken(token);
        List<CourseDataModel> userCreateCourses = COURSE_SERVICE.getAllCourseDataModelByUserId(userId);
        List<CourseDataModel> userPurchasedCourses = USER_COURSE_MAPPING_SERVICE.getAllPurchasedCourses(userId);
        dataBaseModel.setUserModelToSend((UserModelToSend) getUserModelById(userId));
        dataBaseModel.setUserBalanceModel(USER_BALANCE_SERVICE.getUserBalanceModelByUserId(userId));
        dataBaseModel.setUserImageModel(USER_IMAGE_SERVICE.getUserImageModelByUserId(userId));
        dataBaseModel.setUserCreateCourseModels(userCreateCourses);
        dataBaseModel.setUserPurchasedCourseModels(userPurchasedCourses);
        return dataBaseModel;
    }

    private void setForUpdateUser(User user, UpdateUserModel updateUserModel) {
        if (updateUserModel.getUsername() != null)
            user.setUsername(updateUserModel.getUsername());
        if (updateUserModel.getFullName() != null)
            user.setFullName(updateUserModel.getFullName());
        if (updateUserModel.getEmail() != null)
            user.setEmail(updateUserModel.getEmail());
        if (updateUserModel.getPassword() != null)
            user.setPassword(PASSWORD_ENCODER.encode(updateUserModel.getPassword()));
        if (updateUserModel.getBirthDay() != null)
            user.setBirthDay(updateUserModel.getBirthDay());
    }
}