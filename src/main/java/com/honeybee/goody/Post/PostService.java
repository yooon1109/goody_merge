package com.honeybee.goody.Post;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {


    private final Firestore firestore;

    @Autowired
    public PostService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Map<String,Object> getPreviewPosts(String type,int page) throws ExecutionException, InterruptedException {
        //컬렉션참조
        CollectionReference collectionRef = firestore.collection("POST");
        // "createdAt" 필드를 기준으로 내림차순 정렬
        Query query = collectionRef.orderBy("postDate", Query.Direction.ASCENDING);
        int pageSize = 5; // 페이지 크기

        ApiFuture<QuerySnapshot> querySnapshotFuture = query.whereEqualTo("transType", type)
                                                                    .limit(pageSize+1).offset(page * 5).get();
        QuerySnapshot querySnapshot = querySnapshotFuture.get();
        List<PreviewDTO> posts = querySnapshot.getDocuments().stream()
            .map(doc -> {
                PreviewDTO previewDTO = doc.toObject(PreviewDTO.class);
                previewDTO.setDocumentId(doc.getId()); // 문서의 ID를 설정
                return previewDTO;
            })
            .collect(Collectors.toList());

        boolean hasNext = false;
        if(posts.size()>pageSize){
            hasNext=true;
            posts.remove(posts.size()-1);
        }
        Map<String, Object> postInfo = new HashMap<>();
        postInfo.put("postPreviewInfo",posts);//post리스트


        postInfo.put("hasNext",hasNext);
        return postInfo;
    }

}
