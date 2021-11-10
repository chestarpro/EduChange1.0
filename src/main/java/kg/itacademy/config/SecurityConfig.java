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
                .antMatchers(HttpMethod.POST, "/api/user/sign-up").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/sign-in").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/get-all").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/user/update").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/user/delete").permitAll()

                .antMatchers(HttpMethod.POST, "/api/image/create").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/image/update").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/image/delete").authenticated()
                .antMatchers(HttpMethod.GET, "/api/image/get-all").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, "/api/course/create").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/course/update").authenticated()
                .antMatchers(HttpMethod.GET, "/api/course/get-all").authenticated()

                .antMatchers("/api/user/get-current").authenticated()
                .anyRequest().permitAll()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, is_active from users where username=?")
                .authoritiesByUsernameQuery("select u.username, ur.role_name as role from user_role ur inner join " +
                        "users u on ur.user_id = u.id where u.username=? and u.is_active=1");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}