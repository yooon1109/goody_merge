package com.honeybee.goody.Config;

import com.honeybee.goody.Jwt.JwtAuthenticationFilter;
import com.honeybee.goody.Jwt.JwtTokenProvider;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true,prePostEnabled = true)
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig{
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CorsConfig corsConfig;
    //TODO : 로그아웃,

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
             .csrf(c->c.disable())
             .addFilter(corsConfig.corsFilter())
             .headers(f->f.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin()))
             .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .authorizeHttpRequests((request) -> request
                 .requestMatchers("/user/**","/v3/api-docs/**",
                     "/swagger-ui/**","/ws-stomp/**").permitAll()//해당경로 url은 인증없이 사용가능
                 .anyRequest().authenticated())
             .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
             UsernamePasswordAuthenticationFilter.class)//토큰으로 검사
             .logout(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
            .version("v1.0.0")
            .title("API GOODY")
            .description("GOODY 어플 API");

        // SecuritySecheme명
        String jwtSchemeName = "jwtAuth";
        // API 요청헤더에 인증정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        // SecuritySchemes 등록
        Components components = new Components()
            .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                .name(jwtSchemeName)
                .type(SecurityScheme.Type.HTTP) // HTTP 방식
                .scheme("bearer")
                .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)

        return new OpenAPI()
            .info(info)
            .addSecurityItem(securityRequirement)
            .components(components);
    }


}