package kg.itacademy.boot;

import kg.itacademy.entity.Category;
import kg.itacademy.entity.User;
import kg.itacademy.entity.UserRole;
import kg.itacademy.repository.CategoryRepository;
import kg.itacademy.repository.UserRepository;
import kg.itacademy.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

    @Override
    public void run(String... args) throws Exception {
        User admin = new User();
        admin.setFullName("Chyngyz Sheraly uulu");
        admin.setUsername("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword("admin");
        admin.setIsActive(1L);
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);
        userRepository.save(admin);

        UserRole roleAdmin = new UserRole();
        roleAdmin.setRoleName("ROLE_ADMIN");
        roleAdmin.setUser(admin);
        userRoleRepository.save(roleAdmin);

        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().categoryName("SPORT").build());
        categories.add(Category.builder().categoryName("Art").build());
        categories.add(Category.builder().categoryName("Programming").build());
        categories.add(Category.builder().categoryName("DRIVE").build());
        categories.add(Category.builder().categoryName("LANGUAGE").build());

        for (Category category : categories)
            categoryRepository.save(category);
    }
}