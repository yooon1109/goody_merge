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

import java.util.concurrent.ExecutionException;

@Component
public class UserService implements UserDetailsService {
    private final Firestore firestore;

    public UserService(Firestore firestore) {
        this.firestore = firestore;
    }

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        try {
            CollectionReference collectionRef = this.firestore.collection("Users");
            ApiFuture<QuerySnapshot> querySnapshotFuture = collectionRef.whereEqualTo("userId", userId).get();
            QuerySnapshot querySnapshot = (QuerySnapshot)querySnapshotFuture.get();
            if (!querySnapshot.isEmpty()) {
                DocumentSnapshot documentSnapshot = (DocumentSnapshot)querySnapshot.getDocuments().get(0);
                Test user = (Test)documentSnapshot.toObject(Test.class);
                return User
                        .builder()
                        .username(user.getUserId())
                        .password(user.getUserPw())
                        .build();
            } else {
                System.out.println("존재하지 않는 유저");
                return null;
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDetails loginUserInfo() throws ExecutionException, InterruptedException {
        UserDetails customUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CollectionReference collectionRef = this.firestore.collection("Users");
        ApiFuture<QuerySnapshot> querySnapshotFuture = collectionRef.whereEqualTo("userId", customUser.getUsername()).get();
        QuerySnapshot querySnapshot = (QuerySnapshot)querySnapshotFuture.get();
        if (!querySnapshot.isEmpty()) {
            DocumentSnapshot documentSnapshot = (DocumentSnapshot) querySnapshot.getDocuments().get(0);
           // customUser.setUserDocumentId(documentSnapshot.getId());
            return customUser;
        }else {
            System.out.println("존재하지 않는 유저");
            return null;
        }

    }
}