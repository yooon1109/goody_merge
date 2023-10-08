package com.honeybee.goody.Jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // username 과 패스워드로 사용자를 인증하여 액세스토큰을 반환한다.
    public String authenticate(String username, String password) {
        try{
            // 받아온 유저네임과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

            // authenticationToken 객체를 통해 Authentication 객체 생성
            // 이 과정에서 재정의한 loadUserByUsername 메서드 호출
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 인증 정보를 기준으로 jwt access 토큰 생성
            String accessToken = jwtTokenProvider.createToken(authentication);

            return accessToken;

        } catch (BadCredentialsException e) {
                 //인증 실패
                throw new BadCredentialsException("Uncorrect password");
        }


    }

}
