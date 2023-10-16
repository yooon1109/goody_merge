package com.honeybee.goody.MyPage;

import com.google.cloud.firestore.*;
import com.honeybee.goody.User.UserService;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.util.concurrent.TimeUnit;
import java.util.Date;
@Service
public class MyPageService {
    private final Firestore firestore;
    private final UserService userService;

    @Autowired
    public MyPageService(Firestore firestore, UserService userService) throws ExecutionException, InterruptedException {
        this.firestore = firestore;
        this.userService = userService;
    }

    public MyPageHomeDTO getMyPageHome() throws Exception{
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        DocumentSnapshot userDocSnapshot = userDocRef.get().get();

        ModelMapper modelMapper = new ModelMapper();

        if (userDocSnapshot.exists()) {
            MyPageHomeDTO dto = modelMapper.map(userDocSnapshot.getData(), MyPageHomeDTO.class);
            // createdDate 필드를 매핑
            com.google.cloud.Timestamp firestoreTimestamp = userDocSnapshot.get("joinDate", com.google.cloud.Timestamp.class);
            java.util.Date joinDate = firestoreTimestamp.toDate();
            dto.setJoinDate(joinDate);

            // 현재 시간 계산
            Date currentDate = new Date();
            long diffInMillies = currentDate.getTime() - joinDate.getTime();
            long daysSinceJoin = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)+1;
            dto.setDaysSinceJoin(daysSinceJoin);

            return dto;
        } else {
            throw new Exception("없음!!");
        }
    }

}
