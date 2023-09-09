package com.honeybee.goody.Collection;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectionService {

    private final Firestore firestore;
    @Autowired
    public CollectionService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Map<String,Object> getCollectionDetail(String userId, String collectionId) throws Exception {
        Query userQuery = firestore.collection("Users").whereEqualTo("userId", userId);
        QuerySnapshot querySnapshot = userQuery.limit(1).get().get();

        DocumentSnapshot userDoc = querySnapshot.getDocuments().get(0);

        CollectionReference myCollectionRef  = userDoc.getReference().collection("myCollection");

        Query collectionQuery = myCollectionRef.whereEqualTo("collectionId", collectionId);
        QuerySnapshot collectionQuerySnapshot = collectionQuery.get().get();

        DocumentSnapshot targetDoc = null;
        if (!collectionQuerySnapshot.isEmpty()) {
            targetDoc = collectionQuerySnapshot.getDocuments().get(0);
        }

        if (targetDoc != null && targetDoc.exists()) {
            Map<String, Object> data = targetDoc.getData();
            CollectionDetailDTO collectionDTO = targetDoc.toObject(CollectionDetailDTO.class);
            collectionDTO.setCollectionId((String) data.get("collectionId"));
            collectionDTO.setContent((String) data.get("content"));
            collectionDTO.setTitle((String) data.get("title"));
            collectionDTO.setImgPath((List<String>) data.get("imgPath"));

            com.google.cloud.Timestamp firestoreTimestamp = (com.google.cloud.Timestamp) targetDoc.get("uploadDate");
            java.util.Date uploadDate = firestoreTimestamp.toDate();
            collectionDTO.setUploadDate(uploadDate);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("collectionId", collectionDTO.getCollectionId());
            responseData.put("content", collectionDTO.getContent());
            responseData.put("title", collectionDTO.getTitle());
            responseData.put("uploadDate", collectionDTO.getUploadDate());
            responseData.put("imgPath", collectionDTO.getImgPath());

            return responseData;
        } else {
            throw new Exception("없음!!");
        }
    }

    public Map<String,Object> getCollectionList(String userId) throws Exception {
        Query userQuery = firestore.collection("Users").whereEqualTo("userId", userId);
        QuerySnapshot userQuerySnapshot = userQuery.limit(1).get().get();

        DocumentSnapshot userDoc = userQuerySnapshot.getDocuments().get(0);

        CollectionReference myCollectionRef  = userDoc.getReference().collection("myCollection");

        ApiFuture<QuerySnapshot> querySnapshot = myCollectionRef.get();

        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        List<CollectionListDTO> dtoList = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            if (document.exists()) {
                CollectionListDTO dto = new CollectionListDTO();
                dto.setCollectionId(document.getString("collectionId"));
                dto.setThumbnailPath(document.getString("thumbnailPath"));
                dtoList.add(dto);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", dtoList);
        return response;
    }
}
