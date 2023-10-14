package com.honeybee.goody.User;

import com.honeybee.goody.Jwt.AuthService;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class
UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final AuthService authService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String,String> user) {
        //authenticate에서는 인증 실패시 예외처리가 아이디 비번 구분 불가능해서 일단
        userService.loadUserByUsername(user.get("userId"));//유저 아이디 오류 예외처리용으로 사용
        String token = authService.authenticate(user.get("userId"),user.get("userPw"));//
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);//헤더 세팅
        return ResponseEntity.ok().headers(httpHeaders).body(token);
    }

//    @Operation(summary = "마이페이지", description = "유저의 마이페이지 첫 화면")
//    @GetMapping("/userHome")
//    public Map<String, Object> getCollectionList() throws Exception {
//        return collectionService.getCollectionList();
//    }
}
