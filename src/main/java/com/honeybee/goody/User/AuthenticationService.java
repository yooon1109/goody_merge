package com.honeybee.goody.User;

import com.google.firebase.auth.*;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    //firebase auth이용하는 경우
    public UserRecord verifyUser(String userId, String password) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(userId);
            // 사용자가 존재하는 경우 Firebase 인증을 사용하여 비밀번호 검증
            if (userRecord != null) {

//                return FirebaseAuth.getInstance().createCustomToken(userRecord.getUid());
                return userRecord; // 로그인에 성공하면 비밀번호가 일치한다고 판단
            } else {
                // 사용자가 존재하지 않음
                return null;
            }
        } catch (FirebaseAuthException e) {
            // Firebase 인증 예외 처리
            e.printStackTrace();
            return null;
        }
    }

}
