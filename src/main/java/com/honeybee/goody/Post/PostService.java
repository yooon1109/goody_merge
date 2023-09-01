package com.honeybee.goody.Post;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    List<PreviewDTO> sellPosts;

    private final Firestore firestore;

    @Autowired
    public PostService(Firestore firestore) {
        this.firestore = firestore;
    }

    public List<PreviewDTO> getPreviewPosts(String type) throws ExecutionException, InterruptedException {
        //컬렉션참조
        CollectionReference collectionRef = firestore.collection("POST");
        ApiFuture<QuerySnapshot> querySnapshotFuture = collectionRef.whereEqualTo("transType", type).get();
        QuerySnapshot querySnapshot = querySnapshotFuture.get();
        sellPosts = querySnapshot.getDocuments().stream()
            .map(doc->doc.toObject(PreviewDTO.class))
            .collect(Collectors.toList());
        return sellPosts;
    }

}
