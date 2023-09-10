package com.honeybee.goody.Collection;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.google.cloud.storage.*;
import com.google.cloud.storage.Blob;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
            collectionDTO.setImages((List<String>) data.get("images"));

            com.google.cloud.Timestamp firestoreTimestamp = (com.google.cloud.Timestamp) targetDoc.get("createdDate");
            java.util.Date createdDate = firestoreTimestamp.toDate();
            collectionDTO.setCreatedDate(createdDate);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("collectionId", collectionDTO.getCollectionId());
            responseData.put("content", collectionDTO.getContent());
            responseData.put("title", collectionDTO.getTitle());
            responseData.put("createdDate", collectionDTO.getCreatedDate());
            responseData.put("images", collectionDTO.getImages());

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

    public ResponseEntity<String> createCollection(CollectionInputDTO inputData) throws Exception {
        Query userQuery = firestore.collection("Users").whereEqualTo("userId", inputData.getUserId());
        QuerySnapshot userQuerySnapshot = userQuery.limit(1).get().get();

        DocumentSnapshot userDoc = userQuerySnapshot.getDocuments().get(0);

        CollectionReference myCollectionRef = userDoc.getReference().collection("myCollection");

        ApiFuture<QuerySnapshot> future = myCollectionRef.get();
        QuerySnapshot allCollectionsSnapshot = future.get();
        int collectionCount = allCollectionsSnapshot.size();
        int newCollectionCount = collectionCount + 1;

        String newCollectionId = inputData.getUserId() + "-" + newCollectionCount;

        if (userQuerySnapshot.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        List<String> imageUrls = saveImagesToStorage(inputData.getImages());

        Map<String, Object> data = new HashMap<>();
        data.put("collectionId", newCollectionId);
        data.put("title", inputData.getTitle());
        data.put("content", inputData.getContent());
        data.put("images", imageUrls);
        LocalDateTime now = LocalDateTime.now();

        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = now.atZone(zoneId).toInstant();
        Date date = Date.from(instant);

        com.google.cloud.Timestamp firestoreTimestamp = com.google.cloud.Timestamp.of(date);
        java.util.Date createdDate = firestoreTimestamp.toDate();
        data.put("createdDate", createdDate);

        myCollectionRef.add(data);

        return new ResponseEntity<>("Collection added successfully", HttpStatus.CREATED);
    }

    private List<String> saveImagesToStorage(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        try {
            // Firebase Storage 초기화
            Storage storage = StorageOptions.getDefaultInstance().getService();

            for (MultipartFile image : images) {
                String bucketName = FirebaseApp.getInstance().getOptions().getStorageBucket();
                Bucket bucket = StorageClient.getInstance().bucket(bucketName);//'gs://goody-4b16e.appspot.com'
                InputStream content = new ByteArrayInputStream(image.getBytes());
                Blob blob = bucket.create("collections/"+image.getOriginalFilename(),content,image.getContentType());
                imageUrls.add(blob.getName());
            }
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }

        return imageUrls;
    }
}
