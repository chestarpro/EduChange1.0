package kg.itacademy.service.impl;

import kg.itacademy.converter.UserConverter;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserLog;
import kg.itacademy.entity.UserBalance;
import kg.itacademy.entity.UserRole;
import kg.itacademy.exception.ApiFailException;
import kg.itacademy.model.AuthDataBaseUserModel;
import kg.itacademy.model.CourseModel;
import kg.itacademy.model.UserAuthorizModel;
import kg.itacademy.model.UserModel;
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

    private final UserRepository userRepository;

    private final UserRoleService userRoleService;

    private final PasswordEncoder passwordEncoder;

    private final UserLogService userLogService;

    private final UserConverter CONVERTER_CONVERTER;

    @Autowired
    private UserImageService userImageService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserBalanceService userBalanceService;

    @Autowired
    private UserCourseMappingService userCourseMappingService;

    @Override
    public User save(User user) {
        validateVariablesForNullOrIsEmpty(user);
        validateSpace(user);
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        checkUsernameAndEmail(user);
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
    public AuthDataBaseUserModel createUser(User user) {
        String basePassword = user.getPassword();
        User user1 = save(user);

        String usernamePasswordPair = user1.getUsername() + ":" + basePassword;
        String authHeader = new String(Base64.getEncoder().encode(usernamePasswordPair.getBytes()));

        String token = "Basic " + authHeader;

        return getAuthDataBaseUser(token, user1.getId());
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserModel> getAllUserModels() {
        return getAll().stream()
                .map(CONVERTER_CONVERTER::convertFromEntity).collect(Collectors.toList());
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserModel getUserModelById(Long id) {
        return CONVERTER_CONVERTER.convertFromEntity(getById(id));
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
        return CONVERTER_CONVERTER.convertFromEntity(getCurrentUser());
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

        return userRepository.save(user);
    }

    @Override
    public UserModel updateUser(User user) {
        return CONVERTER_CONVERTER.convertFromEntity((update(user)));
    }

    @Override
    public AuthDataBaseUserModel getBasicAuthorizHeaderByAuthorizModel(UserAuthorizModel userAuthorizModel) {
        User user = userRepository.findByUsername(userAuthorizModel.getUsername())
                .orElseThrow(() -> new ApiFailException("Invalid username or password"));

        boolean isPasswordIsCorrect = passwordEncoder.matches(userAuthorizModel.getPassword(), user.getPassword());

        checkBaningStatus(user);
        checkFailPassword(isPasswordIsCorrect, user);

        String usernamePasswordPair = userAuthorizModel.getUsername() + ":" + userAuthorizModel.getPassword();
        System.out.println(userAuthorizModel.getPassword());
        String authHeader = new String(Base64.getEncoder().encode(usernamePasswordPair.getBytes()));
        userLogService.save(new UserLog(user, true));

        String token = "Basic " + authHeader;

        return getAuthDataBaseUser(token, user.getId());
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
        return CONVERTER_CONVERTER.convertFromEntity(deleteUser);
    }

    @Override
    public UserModel deleteUserByAdmin(Long userId) {
        User user = getById(userId);
        User deleteUser = setInActiveUser(user, -1L);
        return CONVERTER_CONVERTER.convertFromEntity(deleteUser);
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
            UserLog userLog = userLogService.getLastLogByUserId(user.getId());
            if (LocalDateTime.now().isAfter(userLog.getCreateDate().plusMinutes(5)))
                setInActiveUser(user, 1L);
            else throw new ApiFailException("You were blocked for 5 minutes");
        }
    }

    private void checkFailPassword(boolean isPasswordIsCorrect, User user) {
        if (!isPasswordIsCorrect) {
            userLogService.save(new UserLog(user, false));

            boolean needToBan = userLogService.hasThreeFailsLastsLogsByUserId(user.getId());

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

    private AuthDataBaseUserModel getAuthDataBaseUser(String token, Long userId) {
        AuthDataBaseUserModel dataBaseModel = new AuthDataBaseUserModel();
        dataBaseModel.setToken(token);
        List<CourseModel> userCreateCourses = courseService.getAllByUserId(userId);
        List<CourseModel> userPurchasedCourses = userCourseMappingService.getAllPurchasedCourses(userId);
        dataBaseModel.setUser(getUserModelById(userId));
        dataBaseModel.setUserBalance(userBalanceService.getUserBalanceModelByUserId(userId));
        dataBaseModel.setUserImage(userImageService.getUserImageModelByUserId(userId));
        dataBaseModel.setUserCreateCourses(userCreateCourses);
        dataBaseModel.setUserPurchasedCourses(userPurchasedCourses);

        return dataBaseModel;
    }
}