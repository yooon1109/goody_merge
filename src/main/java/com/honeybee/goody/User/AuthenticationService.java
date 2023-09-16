package com.honeybee.goody.User;

import com.google.firebase.auth.*;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    //firebase auth이용하는 경우
    public UserRecord verifyUser(String userId, String password) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(userId);//firebase auth에서 유저이메일 존재유무 파악
            // 사용자가 존재하는 경우 Firebase 인증을 사용하여 비밀번호 검증
            if (userRecord != null) {

//                return FirebaseAuth.getInstance().createCustomToken(userRecord.getUid());//토큰 생성..근데 어디다 써..?
                return userRecord; // 유저 정보
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
