package kg.itacademy.service.impl;

import kg.itacademy.converter.UserConverter;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserLog;
import kg.itacademy.entity.UserBalance;
import kg.itacademy.entity.UserRole;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.user.UserProfileDataModel;
import kg.itacademy.model.course.CourseModel;
import kg.itacademy.model.user.UserAuthorizModel;
import kg.itacademy.model.user.UserModel;
import kg.itacademy.repository.UserRepository;
import kg.itacademy.service.*;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, VariableValidation<User> {

    private final UserRepository USER_REPOSITORY;
    private final UserRoleService USER_ROLE_SERVICE;
    private final PasswordEncoder PASSWORD_ENCODER;
    private final UserLogService USER_LOG_SERVICE;
    private final UserConverter USER_CONVERTER;

    @Autowired
    private UserImageService USER_IMAGE_SERVICE;

    @Autowired
    private CourseService COURSE_SERVICE;

    @Autowired
    private UserBalanceService USER_BALANCE_SERVICE;

    @Autowired
    private UserCourseMappingService USER_COURSE_MAPPING_SERVICE;

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
    public UserProfileDataModel createUser(User user) {
        validateVariablesForNullOrIsEmpty(user);
        validateSpace(user);
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        checkUsernameAndEmail(user);
        validateLengthVariables(user);

        String usernamePasswordPair = user.getUsername() + ":" + user.getPassword();
        String authHeader = new String(Base64.getEncoder().encode(usernamePasswordPair.getBytes()));

        String token = "Basic " + authHeader;

        User dataUser = save(user);

        return getUserProfileDataModelByUserId(token, dataUser.getId());
    }

    @Override
    public List<User> getAll() {
        return USER_REPOSITORY.findAll();
    }

    @Override
    public List<UserModel> getAllUserModels() {
        return getAll().stream()
                .map(USER_CONVERTER::convertFromEntity).collect(Collectors.toList());
    }

    @Override
    public User getById(Long id) {
        return USER_REPOSITORY.findById(id).orElse(null);
    }

    @Override
    public UserModel getUserModelById(Long id) {
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
    public UserModel getCurrentUserModel() {
        return USER_CONVERTER.convertFromEntity(getCurrentUser());
    }

    public User update(User user) {
        if (user.getId() == null)
            throw new ApiFailException("User id not specified");

        validateVariablesForNullOrIsEmptyUpdate(user);
        checkUsernameAndEmailForUpdate(user);
        validateLengthVariablesForUpdate(user);
        if (user.getEmail() != null)
            validateEmail(user.getEmail());
        if (user.getUsername() != null)
            validateUsername(user.getUsername());
        validateSpaceForUpdate(user);

        return USER_REPOSITORY.save(user);
    }

    @Override
    public UserModel updateUser(User user) {
        return USER_CONVERTER.convertFromEntity((update(user)));
    }

    @Override
    public UserProfileDataModel getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel) {
        User user = USER_REPOSITORY.findByUsername(userAuthorizModel.getUsername())
                .orElseThrow(() -> new ApiFailException("Invalid username or password"));

        boolean isPasswordIsCorrect = PASSWORD_ENCODER.matches(userAuthorizModel.getPassword(), user.getPassword());

        checkBaningStatus(user);
        checkFailPassword(isPasswordIsCorrect, user);

        String usernamePasswordPair = userAuthorizModel.getUsername() + ":" + userAuthorizModel.getPassword();
        System.out.println(userAuthorizModel.getPassword());
        String authHeader = new String(Base64.getEncoder().encode(usernamePasswordPair.getBytes()));
        USER_LOG_SERVICE.save(new UserLog(user, true));

        String token = "Basic " + authHeader;

        return getUserProfileDataModelByUserId(token, user.getId());
    }

    @Override
    public User setInActiveUser(User user, Long status) {
        user.setIsActive(status);
        return USER_REPOSITORY.save(user);
    }

    @Override
    public UserModel deleteUser() {
        User user = getCurrentUser();
        User deleteUser = setInActiveUser(user, -1L);
        return USER_CONVERTER.convertFromEntity(deleteUser);
    }

    @Override
    public UserModel deleteUserByAdmin(Long userId) {
        User user = getById(userId);
        User deleteUser = setInActiveUser(user, -1L);
        return USER_CONVERTER.convertFromEntity(deleteUser);
    }

    @Override
    public void validateVariablesForNullOrIsEmpty(User user) {
        if (user.getFullName() == null || user.getFullName().isEmpty())
            throw new ApiFailException("Full name is not filled");
        if (user.getUsername() == null || user.getUsername().isEmpty())
            throw new ApiFailException("Username is not filled");
        if (user.getEmail() == null || user.getEmail().isEmpty())
            throw new ApiFailException("Email is not filled");
        if (user.getPassword() == null || user.getPassword().isEmpty())
            throw new ApiFailException("Password is not filled");
    }

    @Override
    public void validateVariablesForNullOrIsEmptyUpdate(User user) {
        if (user.getEmail() != null && user.getFullName().isEmpty())
            throw new ApiFailException("Full name is not filled");

        if (user.getUsername() != null && user.getUsername().isEmpty())
            throw new ApiFailException("Username is not filled");

        if (user.getEmail() != null && user.getEmail().isEmpty())
            throw new ApiFailException("Email is not filled");

        if (user.getPassword() != null && user.getPassword().isEmpty())
            throw new ApiFailException("Password is not filled");
    }

    @Override
    public void validateLengthVariables(User user) {
        if (user.getFullName().length() > 100)
            throw new ApiFailException("Exceeded character limit (100) for full name");

        if (user.getUsername().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for username");

        if (user.getEmail().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for email");

        if (user.getPassword().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for password");

        if (user.getPassword().length() < 6)
            throw new ApiFailException("The number of password characters must be more than 5");
    }

    @Override
    public void validateLengthVariablesForUpdate(User user) {
        if (user.getFullName() != null && user.getFullName().length() > 100)
            throw new ApiFailException("Exceeded character limit (100) for full name");

        if (user.getUsername() != null && user.getUsername().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for username");

        if (user.getEmail() != null && user.getEmail().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for email");

        if (user.getPassword() != null && user.getPassword().length() > 50)
            throw new ApiFailException("Exceeded character limit (50) for password");

        else if (user.getPassword() != null && user.getPassword().length() < 6)
            throw new ApiFailException("The number of password characters must be more than 5");
    }

    private void checkUsernameAndEmail(User user) {
        User dataUserByUserName = getByUsername(user.getUsername());
        User dataUserByEmail = getByEmail(user.getEmail());

        if (dataUserByUserName != null)
            throw new ApiFailException("Such user " + dataUserByUserName.getUsername() + " already exists");

        if (dataUserByEmail != null)
            throw new ApiFailException("Email " + dataUserByEmail.getEmail() + " is already in use");
    }

    private void checkUsernameAndEmailForUpdate(User user) {
        User dataUserByUserName = getByUsername(user.getUsername());
        User dataUserByEmail = getByEmail(user.getEmail());

        if (user.getUsername() != null && dataUserByUserName != null)
            throw new ApiFailException("Such user " + dataUserByUserName.getUsername() + " already exists");

        if (user.getEmail() != null && dataUserByEmail != null)
            throw new ApiFailException("Email " + dataUserByEmail.getEmail() + " is already in use");
    }

    private void checkBaningStatus(User user) {
        if (user.getIsActive() == 0) {
            UserLog userLog = USER_LOG_SERVICE.getLastLogByUserId(user.getId());
            if (LocalDateTime.now().isAfter(userLog.getCreateDate().plusMinutes(5)))
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

    @Override
    public void validateEmail(String email) {
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Matcher matcher = Pattern.compile(emailRegex).matcher(email);
        if (!matcher.matches()) {
            throw new ApiFailException("Incorrect email format");
        }
    }

    private void validateUsername(String email) {
        String usernameRegex = "^[a-zA-Z0-9._-]{3,}$";
        Matcher matcher = Pattern.compile(usernameRegex).matcher(email);
        if (!matcher.matches()) {
            throw new ApiFailException("Incorrect login format");
        }
    }

    private void validateSpace(User user) {
        if (user.getUsername().contains(" "))
            throw new ApiFailException("Invalid username format");
        if (user.getEmail().contains(" "))
            throw new ApiFailException("Invalid email format");
    }

    private void validateSpaceForUpdate(User user) {
        if (user.getUsername() != null && user.getUsername().contains(" "))
            throw new ApiFailException("Invalid username format");
        if (user.getUsername() != null && user.getEmail().contains(" "))
            throw new ApiFailException("Invalid email format");
    }

    private UserProfileDataModel getUserProfileDataModelByUserId(String token, Long userId) {
        UserProfileDataModel dataBaseModel = new UserProfileDataModel();
        dataBaseModel.setToken(token);
        List<CourseModel> userCreateCourses = COURSE_SERVICE.getAllByUserId(userId);
        List<CourseModel> userPurchasedCourses = USER_COURSE_MAPPING_SERVICE.getAllPurchasedCourses(userId);
        dataBaseModel.setUser(getUserModelById(userId));
        dataBaseModel.setUserBalance(USER_BALANCE_SERVICE.getUserBalanceModelByUserId(userId));
        dataBaseModel.setUserImage(USER_IMAGE_SERVICE.getUserImageModelByUserId(userId));
        dataBaseModel.setUserCreateCourses(userCreateCourses);
        dataBaseModel.setUserPurchasedCourses(userPurchasedCourses);

        return dataBaseModel;
    }
}