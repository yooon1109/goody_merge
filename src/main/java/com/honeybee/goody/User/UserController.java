package com.honeybee.goody.User;

import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    @GetMapping("/get")
    public ResponseEntity<String> userLogin() throws ExecutionException, InterruptedException {

        //UserRecord user = authenticationService.verifyUser(userId,password);
        UserDetails customUser = userService.loginUserInfo();
        return ResponseEntity.ok(customUser.getUsername());
    }
}
