package com.honeybee.goody.Config;

import com.honeybee.goody.User.UserService;
import jakarta.servlet.DispatcherType;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class SecurityConfig {
    UserService userService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

         http
                .csrf(c->c.disable())
                //.cors((c) -> c.disable())
                .cors((c) -> {
                    CorsConfigurationSource source = (request) -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*"));
                    config.setAllowedMethods(List.of("*"));
                    return config;
            };
            c.configurationSource(source);
        })
                .authorizeHttpRequests((request) -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/login").permitAll().anyRequest().authenticated())

                .formLogin(login -> login
//                        .loginPage("http://localhost:3000/")//만들어둔 로그인 페이지
                        .loginProcessingUrl("/login")
                        .usernameParameter("userId")
                        .passwordParameter("userPw")
                    //    .defaultSuccessUrl("/docs")
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
                            response.sendRedirect("/goody/docs");
//                            System.out.println(passwordEncoder().encode("qwe123"));//암호화한 비밀번호 알아내기
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                        })
                        )
                .logout(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}