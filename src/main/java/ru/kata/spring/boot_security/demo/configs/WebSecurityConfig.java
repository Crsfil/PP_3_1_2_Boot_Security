package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final AuthenticationSuccessHandler successUserHandler;
    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(AuthenticationSuccessHandler successUserHandler,
                             UserDetailsService userDetailsService) {
        this.successUserHandler = successUserHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Все админские URL
                        .requestMatchers("/user").hasAnyRole("USER", "ADMIN") // Доступ для USER и ADMIN
                        .requestMatchers("/login").permitAll() // Разрешить доступ к странице логина
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Страница логина
                        .loginProcessingUrl("/login") // URL для обработки формы логина
                        .failureUrl("/login?error") // URL при ошибке аутентификации
//                        .successHandler(successUserHandler) // Использование кастомного обработчика успеха
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // URL после выхода
                        .permitAll()
                )
                .userDetailsService(userDetailsService)
                .csrf(AbstractHttpConfigurer::disable); // Отключение CSRF, если необходимо
        return http.build();
    }
}