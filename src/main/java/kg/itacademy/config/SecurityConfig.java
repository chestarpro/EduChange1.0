package kg.itacademy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.PUT, "/api/user/update").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/user/delete").authenticated()
                .antMatchers(HttpMethod.GET, "/api/user/get-by-id/{id}").authenticated()

                .antMatchers(HttpMethod.POST, "/api/course-image/create/{courseId}").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/course-image/update/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/course-image/delete/{id}").authenticated()

                .antMatchers(HttpMethod.POST, "/api/course/create").authenticated()
                .antMatchers(HttpMethod.PUT, "api/course/update").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/course/delete/{id}").authenticated()

                .antMatchers(HttpMethod.POST, "/api/course-image/create/{courseId}").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/course-image/update/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/course-image/delete/{id}").authenticated()

                .antMatchers(HttpMethod.POST, "/api/user-image/create").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/user-image/update").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/user-image/delete").authenticated()

                .antMatchers(HttpMethod.POST, "/api/purchase/create-by-course-id/{courseId}").authenticated()
                .antMatchers(HttpMethod.GET, "/api/purchase/get-all-purchased-curses/{userId}").authenticated()
                .antMatchers(HttpMethod.GET, "/api/purchase/get-by-id/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/get-all-customers/by-course-id/{courseId}").authenticated()

                .antMatchers(HttpMethod.PUT, "/api/balance/update").authenticated()
                .antMatchers(HttpMethod.GET, "/api/balance/get-by-user-id/{userId}").authenticated()
                .antMatchers(HttpMethod.GET, "/api/balance/get-by-id/{id}").authenticated()

                .antMatchers(HttpMethod.POST, "/api/like/create/{courseId}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/like/delete/{courseId}").authenticated()

                .antMatchers(HttpMethod.POST, "/api/lesson/create").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/lesson/update").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/lesson/delete/{id}").authenticated()

                .antMatchers(HttpMethod.POST, "/api/course-program/create").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/course-program/update").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/course-program/delete/{id}").authenticated()

                .antMatchers(HttpMethod.POST, "/api/comment/create").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/comment/update").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/comment/delete/{id}").authenticated()

                .anyRequest().permitAll()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, is_active from users where username = ?")
                .authoritiesByUsernameQuery("select u.username, ur.role_name as role from user_role ur inner join " +
                        "users u on ur.user_id = u.id where u.username = ? and u.is_active = 1");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}