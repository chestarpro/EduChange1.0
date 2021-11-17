package kg.itacademy.boot;

import kg.itacademy.entity.*;
import kg.itacademy.repository.CategoryRepository;
import kg.itacademy.repository.UserBalanceRepository;
import kg.itacademy.repository.UserRepository;
import kg.itacademy.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationStartRunner implements CommandLineRunner {

    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Override
    public void run(String... args) throws Exception {
        User adminChyngyz = new User();
        adminChyngyz.setFullName("Chyngyz");
        adminChyngyz.setUsername("admin");
        adminChyngyz.setEmail("admin@gmail.com");
        adminChyngyz.setPassword("admin");
        adminChyngyz.setIsActive(1L);
        String encodedPassword = passwordEncoder.encode(adminChyngyz.getPassword());
        adminChyngyz.setPassword(encodedPassword);
        userRepository.save(adminChyngyz);

        UserRole roleAdmin = new UserRole();
        roleAdmin.setRoleName("ROLE_ADMIN");
        roleAdmin.setUser(adminChyngyz);
        userRoleRepository.save(roleAdmin);

        userBalanceRepository.save(UserBalance.builder()
                .user(adminChyngyz)
                .balance(new BigDecimal(1000_000))
                .build());

        User userAlinur = new User();
        userAlinur.setFullName("Alinur");
        userAlinur.setUsername("alinur05");
        userAlinur.setEmail("dubaiprinc247@gmailcom");
        userAlinur.setPassword("123456");
        userAlinur.setIsActive(1L);
        String encodedPasswordUser1 = passwordEncoder.encode(userAlinur.getPassword());
        userAlinur.setPassword(encodedPasswordUser1);
        userRepository.save(userAlinur);

        UserRole roleUser1 = new UserRole();
        roleUser1.setRoleName("ROLE_USER");
        roleUser1.setUser(userAlinur);
        userRoleRepository.save(roleUser1);

        userBalanceRepository.save(UserBalance.builder()
                .user(userAlinur)
                .balance(new BigDecimal(1000_000))
                .build());

        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().categoryName("Sport").build());
        categories.add(Category.builder().categoryName("Art").build());
        categories.add(Category.builder().categoryName("Programming").build());
        categories.add(Category.builder().categoryName("Driving").build());
        categories.add(Category.builder().categoryName("Languages").build());

        for (Category category : categories)
            categoryRepository.save(category);


        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder()
                        .category(categories.get(2))
                        .courseName("Java for dummies")
                        .email("java@gmail.com")
                        .phoneNumber("0707 223 327")
                        .courseShortInfo("Basic knowledge of Java")
                        .courseInfo("")
                .build());




    }
}