package com.honeybee.goody.Test;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Service
public class TestService {

    Test user;
    private final Firestore firestore;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public TestService(Firestore firestore, PasswordEncoder passwordEncoder) {
        this.firestore = firestore;
        this.passwordEncoder = passwordEncoder;// userPw를 암호화
    }

    public Test getUser(String collection, String id) throws ExecutionException, InterruptedException {

        //컬렉션참조
        CollectionReference collectionRef = firestore.collection(collection);
        // "id" 필드가 id인 문서를 쿼리하여 가져오기
        ApiFuture<QuerySnapshot> querySnapshotFuture = collectionRef.whereEqualTo("id", id).get();
        QuerySnapshot querySnapshot = querySnapshotFuture.get();
        if(!querySnapshot.isEmpty()){
            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
            user = documentSnapshot.toObject(Test.class);
        }else{
            System.out.println("존재하지 않는 유저");
        }
        return user;
//        FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance();
//        Firestore firestore = firestoreOptions.getService();
//        DocumentReference docRef = collectionRef.document("user123"); //
//        ApiFuture<DocumentSnapshot> future = docRef.get();
//        DocumentSnapshot documentSnapshot = future.get();

//        Test user = null;
//        if(documentSnapshot.exists()){
//            user = documentSnapshot.toObject(Test.class);
//        }

    }

    public String userJoin(UserJoinDTO userJoinDTO) throws ExecutionException, InterruptedException {

        String encodedPassword = passwordEncoder.encode(userJoinDTO.getUserPw());

        // DTO의 데이터와 추가적인 데이터를 Map에 저장
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userJoinDTO.getUserId());
        userData.put("userPw", encodedPassword);
        userData.put("birth", userJoinDTO.getBirth());
        userData.put("userName", userJoinDTO.getUserName());
        userData.put("userPhoneNum", userJoinDTO.getUserPhoneNum());

        userData.put("accountBank", "입력해주세요");
        userData.put("accountNum", "입력해주세요");
        userData.put("address", "입력해주세요");
        userData.put("nickname", "입력해주세요");
        userData.put("collectionCnt", 0);
        userData.put("grade", "애기꿀벌");

        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = now.atZone(zoneId).toInstant();
        Date date = Date.from(instant);
        Timestamp firestoreTimestamp = Timestamp.of(date);
        Date createdDate = firestoreTimestamp.toDate();
        userData.put("joinDate", createdDate);

        //컬렉션참조
        CollectionReference collectionRef = firestore.collection("Users");
        collectionRef.add(userData);
        return userJoinDTO.getUserId()+"성공";
    }
}
