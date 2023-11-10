package com.honeybee.goody.User;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        try {
            //컬렉션참조
            CollectionReference collectionRef = firestore.collection("Users");
            collectionRef.add(user);
            return userJoinDTO.getUserId() + "성공";
        } catch (Exception e) {
            throw new RuntimeException("유저 추가 중 오류발생", e);
        }
    }
}
