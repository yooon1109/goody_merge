package com.honeybee.goody.Post;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.honeybee.goody.Test.Test;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    Post sellPost;
    List<Post> sellPosts;
    Firestore dbFirestore = FirestoreClient.getFirestore();// Firestore 초기화
    public List<Post> getPreviewPosts(String type) throws ExecutionException, InterruptedException {
        //컬렉션참조
        CollectionReference collectionRef = dbFirestore.collection("POST");
        ApiFuture<QuerySnapshot> querySnapshotFuture = collectionRef.whereEqualTo("transType", type).get();
        QuerySnapshot querySnapshot = querySnapshotFuture.get();
        sellPosts = querySnapshot.getDocuments().stream()
            .map(doc->doc.toObject(Post.class))
            .collect(Collectors.toList());
        return sellPosts;
    }

}
