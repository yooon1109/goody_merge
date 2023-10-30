package com.honeybee.goody.User;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.honeybee.goody.Test.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

@Component
@Service

public class UserService implements UserDetailsService {
    private final Firestore firestore;

    public UserService(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        try {
            CollectionReference collectionRef = this.firestore.collection("Users");
            ApiFuture<QuerySnapshot> querySnapshotFuture = collectionRef.whereEqualTo("userId", userId).get();
            QuerySnapshot querySnapshot = (QuerySnapshot)querySnapshotFuture.get();
            if (!querySnapshot.isEmpty()) {
                DocumentSnapshot documentSnapshot = (DocumentSnapshot)querySnapshot.getDocuments().get(0);
                com.honeybee.goody.User.User user = (com.honeybee.goody.User.User) documentSnapshot.toObject(
                    com.honeybee.goody.User.User.class);

                return User
                    .builder()
                    .username(user.getUserId())
                    .password(user.getUserPw())
                    .authorities(String.valueOf(Collections.singletonList("ROLE_USER")))//권한부여 일단은 임시로 user
                    .build();//유저 정보

            }else {
                throw new UsernameNotFoundException("User not found with username: " + userId);

            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    //현재 로그인 한 사람의 정보를 통해 그 유저의 documentId값을 찾아오기
    public String loginUserDocumentId() throws ExecutionException, InterruptedException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CollectionReference collectionRef = this.firestore.collection("Users");
        ApiFuture<QuerySnapshot> querySnapshotFuture = collectionRef.whereEqualTo("userId", userDetails.getUsername()).get();
        QuerySnapshot querySnapshot = (QuerySnapshot)querySnapshotFuture.get();
        if (!querySnapshot.isEmpty()) {
            DocumentSnapshot documentSnapshot = (DocumentSnapshot) querySnapshot.getDocuments().get(0);
            return documentSnapshot.getId();
        }else {
            System.out.println("존재하지 않는 유저");
            return null;
        }

    }

    //회원가입
//    public String userJoin(UserJoinDTO userJoinDTO) throws ExecutionException, InterruptedException {
//
//        String encodedPassword = passwordEncoder.encode(userJoinDTO.getUserPw());
//        userJoinDTO.setUserPw(encodedPassword);
//
//        //컬렉션참조
//        CollectionReference collectionRef = firestore.collection("Users");
//        ApiFuture<DocumentReference> result = collectionRef.add(userJoinDTO);
//        return userJoinDTO.getUserId()+"성공";
//    }
}