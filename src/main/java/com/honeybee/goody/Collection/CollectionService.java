package com.honeybee.goody.Collection;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.google.cloud.storage.*;
import com.google.cloud.storage.Blob;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.honeybee.goody.Contents.PreviewDTO;
import com.honeybee.goody.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CollectionService {

    private final Firestore firestore;
    private final UserService userService;

    @Autowired
    public CollectionService(Firestore firestore, UserService userService) throws ExecutionException, InterruptedException {
        this.firestore = firestore;
        this.userService = userService;
    }

    public Map<String,Object> getCollectionDetail(String collectionId) throws Exception {
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);

        CollectionReference myCollectionRef = userDocRef.collection("myCollection");

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
            List<String> images = collectionDTO.getImages().stream().map(image -> {
                try {
                    String encodedURL = URLEncoder.encode(image, "UTF-8");
                    return "https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/"+ encodedURL + "?alt=media&token=";
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }).toList();

            responseData.put("images", images);

            return responseData;
        } else {
            throw new Exception("없음!!");
        }
    }

    public Map<String,Object> getCollectionList() throws Exception {
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        CollectionReference myCollectionRef = userDocRef.collection("myCollection");
        ApiFuture<QuerySnapshot> querySnapshot = myCollectionRef.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        List<CollectionListDTO> dtoList = documents.stream().map(document -> {
            Map<String, Object> data = document.getData();
            CollectionListDTO dto = document.toObject(CollectionListDTO.class);

            dto.setCollectionId(data.get("collectionId").toString());
            List<String> images = (List<String>) data.get("images");
            String thumbnail = images.get(0).toString();

            try {
                String encodedURL = URLEncoder.encode(thumbnail, "UTF-8");
                dto.setThumbnail("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/"+ encodedURL + "?alt=media&token=");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            return dto;
        }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("data", dtoList);
        return response;
    }

    public ResponseEntity<String> createCollection(CollectionInputDTO inputData) throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CollectionReference collectionRef = this.firestore.collection("Users");
        ApiFuture<QuerySnapshot> future = collectionRef.whereEqualTo("userId", userDetails.getUsername()).get();
        QuerySnapshot querySnapshot = future.get();

        DocumentSnapshot userDoc = querySnapshot.getDocuments().get(0);
        CollectionReference myCollectionRef = userDoc.getReference().collection("myCollection");

        ApiFuture<QuerySnapshot> collectionFuture = myCollectionRef.get();
        QuerySnapshot collectionSnapshot = collectionFuture.get();
        int currentCollectionCount = collectionSnapshot.size();

        int newCollectionCount = currentCollectionCount + 1;

        String newCollectionId = userDetails.getUsername() + "-" + newCollectionCount;

        List<String> imageUrls = saveImagesToStorage(newCollectionId, inputData.getImages());

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

    private List<String> saveImagesToStorage(String collectionId, List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        try {
            // Firebase Storage 초기화
            Storage storage = StorageOptions.getDefaultInstance().getService();

            for (MultipartFile image : images) {
                String bucketName = FirebaseApp.getInstance().getOptions().getStorageBucket();
                Bucket bucket = StorageClient.getInstance().bucket(bucketName);//'gs://goody-4b16e.appspot.com'
                InputStream content = new ByteArrayInputStream(image.getBytes());
                Blob blob = bucket.create("collections/"+collectionId+"-"+image.getOriginalFilename(),content,image.getContentType());
                imageUrls.add(blob.getName());
            }
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }

        return imageUrls;
    }
}
