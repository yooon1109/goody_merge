package com.honeybee.goody.Collection;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.honeybee.goody.User.UserService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.modelmapper.ModelMapper;

@Service
public class CollectionService {

    private final Firestore firestore;
    private final UserService userService;

    @Autowired
    public CollectionService(Firestore firestore, UserService userService) throws ExecutionException, InterruptedException {
        this.firestore = firestore;
        this.userService = userService;
    }

    public CollectionDetailDTO getCollectionDetail(String collectionId) throws Exception {
        String userDocumentId = userService.loginUserDocumentId();
        CollectionReference myCollectionRef = firestore.collection("Users").document(userDocumentId).collection("myCollection");
        DocumentSnapshot documentSnapshot = myCollectionRef.whereEqualTo("collectionId", collectionId).get().get().getDocuments().get(0);

        ModelMapper modelMapper = new ModelMapper();

        if (documentSnapshot.exists()) {
            CollectionDetailDTO collectionDTO = modelMapper.map(documentSnapshot.getData(), CollectionDetailDTO.class);

            // createdDate 필드를 매핑
            com.google.cloud.Timestamp firestoreTimestamp = documentSnapshot.get("createdDate", com.google.cloud.Timestamp.class);
            java.util.Date createdDate = firestoreTimestamp.toDate();
            collectionDTO.setCreatedDate(createdDate);

            // images를 변환하고 매핑
            List<String> images = collectionDTO.getImages().stream().map(image -> {
                try {
                    String encodedURL = URLEncoder.encode(image, "UTF-8");
                    return "https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/"+ encodedURL + "?alt=media&token=";
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }).toList();
            collectionDTO.setImages(images);

            return collectionDTO;
        } else {
            throw new Exception("없음!!");
        }
    }

    public Map<String,Object> getCollectionList() throws Exception {
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        List<QueryDocumentSnapshot> documents = userDocRef.collection("myCollection").orderBy("createdDate", Query.Direction.DESCENDING).get().get().getDocuments();


        List<CollectionListDTO> dtoList = documents.stream().map(document -> {
            Map<String, Object> data = document.getData();
            CollectionListDTO dto = document.toObject(CollectionListDTO.class);

            dto.setCollectionId(data.get("collectionId").toString());

            // 여기에서 "images" 값이 비어있는지 확인, 비어있으면 NULL로 반환, 있으면 URL로 반환
            List<String> images = (List<String>) data.getOrDefault("images", Collections.emptyList());
            String thumbnail = !images.isEmpty() ? images.get(0).toString() : "NULL";

            String encodedURL;
            if (thumbnail.equals("NULL")) {
                encodedURL = "NULL";
            }
            else {
                try {
                    encodedURL = URLEncoder.encode(thumbnail, "UTF-8");
                    dto.setThumbnail("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/" + encodedURL + "?alt=media&token=");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
                return dto;
            }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("dto", dtoList);
        return response;
    }

    public ResponseEntity<String> createCollection(CollectionInputDTO inputData) throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //도큐먼트 아이디로 유저 컬렉션 정보 가져옴
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        CollectionReference myCollectionRef = userDocRef.collection("myCollection");

        //유저의 컬렉션카운트 필드 값 가져옴
        Long collectionCnt = userDocRef.get().get().getLong("collectionCnt");
        //컬렉션 카운트 값 증가해서 넣기
        Map<String, Object> updates = new HashMap<>();
        updates.put("collectionCnt", (Long)collectionCnt+1);
        userDocRef.update(updates);

        long newCollectionCnt = collectionCnt+1;
        //작성하는 컬렉션의 컬렉션 아이디 만들어줌
        String newCollectionId = userDetails.getUsername() + "-" + newCollectionCnt;
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

        Timestamp firestoreTimestamp = Timestamp.of(date);
        Date createdDate = firestoreTimestamp.toDate();
        data.put("createdDate", createdDate);

        myCollectionRef.add(data);

        return ResponseEntity.ok("잘 됨!");


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
