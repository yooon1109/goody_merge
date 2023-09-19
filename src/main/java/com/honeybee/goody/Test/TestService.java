package com.honeybee.goody.Test;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    Test user;
    private final Firestore firestore;

    public TestService(Firestore firestore) {
        this.firestore = firestore;
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

    public String setUser(Test user) throws ExecutionException, InterruptedException {
        //컬렉션참조
        CollectionReference collectionRef =firestore.collection("USER");
        ApiFuture<DocumentReference> result = collectionRef.add(user);
        return user.getUserId()+"성공";
    }
}
