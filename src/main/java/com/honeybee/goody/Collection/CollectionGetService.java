package com.honeybee.goody.Collection;

import com.google.cloud.firestore.*;

import java.sql.Timestamp;
import java.util.*;

import com.google.firebase.cloud.FirestoreClient;
import com.honeybee.goody.Post.PreviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectionGetService {

    private final Firestore firestore;
    @Autowired
    public CollectionGetService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Map<String,Object> getCollectionInfo(String userId, String collectionId) throws Exception {
        // "Users" 컬렉션에서 "mjc123" userId를 갖는 document를 찾는다
        Query userQuery = firestore.collection("Users").whereEqualTo("userId", userId);
        QuerySnapshot querySnapshot = userQuery.limit(1).get().get();

        DocumentSnapshot userDoc = querySnapshot.getDocuments().get(0);

        // userDocument 내부의 "myCollection" 컬렉션에서 "mjc1" collectionId를 갖는 document를 찾는다
        CollectionReference myCollectionRef  = userDoc.getReference().collection("myCollection");

        // "myCollection" 서브 컬렉션 내에서 "mjc1" collectionId를 가진 문서를 찾습니다.
        Query collectionQuery = myCollectionRef.whereEqualTo("collectionId", collectionId);
        QuerySnapshot collectionQuerySnapshot = collectionQuery.get().get();

        // 결과에서 첫 번째 문서를 가져옵니다.
        DocumentSnapshot targetDoc = null;
        if (!collectionQuerySnapshot.isEmpty()) {
            targetDoc = collectionQuerySnapshot.getDocuments().get(0);
        }

        if (targetDoc != null && targetDoc.exists()) {
            Map<String, Object> data = targetDoc.getData();
            CollectionDTO collectionDTO = targetDoc.toObject(CollectionDTO.class);
            collectionDTO.setCollectionId((String) data.get("collectionId"));
            collectionDTO.setContent((String) data.get("content"));
            collectionDTO.setTitle((String) data.get("title"));
            collectionDTO.setImgPath((List<String>) data.get("imgPath"));
            collectionDTO.setThumbnailPath((String) data.get("thumbnailPath"));

            com.google.cloud.Timestamp firestoreTimestamp = (com.google.cloud.Timestamp) targetDoc.get("uploadDate");
            java.util.Date uploadDate = firestoreTimestamp.toDate();
            collectionDTO.setUploadDate(uploadDate);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("collectionId", collectionDTO.getCollectionId());
            responseData.put("content", collectionDTO.getContent());
            responseData.put("title", collectionDTO.getTitle());
            responseData.put("uploadDate", collectionDTO.getUploadDate());
            responseData.put("tnumbnailPath", collectionDTO.getThumbnailPath());
            responseData.put("imgPath", collectionDTO.getImgPath());

            return responseData;
        } else {
            throw new Exception("없음!!");
        }
    }
}
