package com.honeybee.goody.User;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import java.util.HashMap;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;


import java.util.Date;

@Service
public class JoinService {
    private final Firestore firestore;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public JoinService(Firestore firestore, PasswordEncoder passwordEncoder) {
        this.firestore = firestore;
        this.passwordEncoder = passwordEncoder;// userPw를 암호화
    }

    public String userJoin(UserJoinDTO userJoinDTO) {

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userJoinDTO, User.class);

        String encodedPassword = passwordEncoder.encode(userJoinDTO.getUserPw());
        user.setUserPw(encodedPassword);

        Date currentDate = new Date();
        user.setJoinDate(currentDate);

        Map<String,Integer> keywords = new HashMap<>();
        for(int i=1;i<=6;i++){
            keywords.put("good"+i,0);
            keywords.put("bad"+i,0);
        }
        user.setKeywords(keywords);
        user.setGrade("애벌레");
        user.setAddress("입력해주세요");
        user.setAccountBank("입력해주세요");
        user.setAccountNum("입력해주세요");
        user.setProfileImg("Null");

        try {
            //컬렉션참조
            CollectionReference collectionRef = firestore.collection("Users");
            collectionRef.add(user);
            return userJoinDTO.getUserId() + "성공";
        } catch (Exception e) {
            throw new RuntimeException("유저 추가 중 오류발생", e);
        }
    }

    public boolean isUserIdAvailable(String userId) {
        CollectionReference users = firestore.collection("Users");
        Query query = users.whereEqualTo("userId", userId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            return querySnapshot.get().isEmpty(); // 결과가 비어있다면 사용 가능
        } catch (Exception e) {
            throw new RuntimeException("아이디 확인 중 오류 발생", e);
        }
    }
}
