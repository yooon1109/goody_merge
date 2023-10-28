package com.honeybee.goody.Test;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {
    Test user;
    private final TestService testService;

    @Operation(summary = "유저 검색", description = "파라미터 값(id)에 해당하는 조건의 유저를 검색.")
    @GetMapping("/get")// ?id="yooon"
    public ResponseEntity<String> test(@RequestParam(value = "id")String id) {

        try {
            user = testService.getUser("USER",id);//USER컬렉션 밑에 id값에 해당하는 유저 찾기
        } catch (ExecutionException | InterruptedException e) {
            // 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (user != null) {
            return ResponseEntity.ok(user.getUserId()+"있음");
        } else {
            return ResponseEntity.ok("존재하지않는유저");
        }
    }

    @Operation(summary = "유저 상세페이지", description = "유저 검색 먼저 해야 작동함")
    @GetMapping("/get/{id}")//
    public ResponseEntity<Test> testGet2(@PathVariable("id") String id) throws ExecutionException, InterruptedException {
        user = testService.getUser("USER",id);//USER컬렉션 밑에 id값에 해당하는 유저 찾기
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinDTO userJoinDTO) throws ExecutionException, InterruptedException {
        String newUser = testService.userJoin(userJoinDTO);
        return ResponseEntity.ok(newUser);
    }

}
