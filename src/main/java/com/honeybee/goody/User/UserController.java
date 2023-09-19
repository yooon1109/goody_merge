package com.honeybee.goody.User;

import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    @GetMapping("/get")
    public ResponseEntity<String> userLogin() throws ExecutionException, InterruptedException {

        //UserRecord user = authenticationService.verifyUser(userId,password);//firebase auth로 정보 가져오기
        String userDocumentId = userService.loginUserDocumentId();

        return ResponseEntity.ok(userDocumentId);
    }
}
