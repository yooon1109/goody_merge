package com.honeybee.goody.Collection;

import com.google.api.core.ApiFuture;
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

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.modelmapper.ModelMapper;


@Service
@RequiredArgsConstructor
public class CollectionService {

    private final Firestore firestore;
    private final UserService userService;

    //컬렉션 상세페이지
    public CollectionDetailDTO getCollectionDetail(String documentId) throws Exception {
        String userDocumentId = userService.loginUserDocumentId();
        CollectionReference collectionRef = firestore.collection("Collections");
        DocumentSnapshot documentSnapshot = collectionRef.document(documentId).get().get();

        if (documentSnapshot.exists()) {
            Collection collection = documentSnapshot.toObject(Collection.class);
            ModelMapper modelMapper = new ModelMapper();
            CollectionDetailDTO detailDTO = modelMapper.map(collection, CollectionDetailDTO.class);

            // createdDate 필드를 매핑
            com.google.cloud.Timestamp firestoreTimestamp = documentSnapshot.get("createdDate", com.google.cloud.Timestamp.class);
            java.util.Date createdDate = firestoreTimestamp.toDate();
            detailDTO.setCreatedDate(createdDate);

            // images를 변환하고 매핑
            List<String> images = detailDTO.getFilePath().stream().map(image -> {
                try {
                    String encodedURL = URLEncoder.encode(image, "UTF-8");
                    return "https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/"+ encodedURL + "?alt=media&token=";
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }).toList();
            detailDTO.setFilePath(images);

            detailDTO.setCollectionId(documentId);

            //내 글인지 확인
            if(detailDTO.getUserId().equals(userDocumentId)){
                detailDTO.setMyCollection(true);
            }
            else detailDTO.setMyCollection(false);

            //좋아요 했던 컬렉션인지 확인
            DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
            List<String> likes = (List<String>) userDocRef.get().get().get("collectionLikes");
            for(String like : likes){
                if(like.equals(documentId)){
                    detailDTO.setLiked(true);
                }else{
                    detailDTO.setLiked(false);
                }
            }

            return detailDTO;
        } else {
            throw new Exception("없음!!");
        }
    }
    //유저별 컬렉션 미리보기(리스트)
    public Map<String, Object> getCollectionList() throws Exception {
        String userDocumentId = userService.loginUserDocumentId();
        CollectionReference collectionRef = firestore.collection("Collections");
        ApiFuture<QuerySnapshot> querySnapshotFuture = collectionRef.whereEqualTo("userId", userDocumentId).get();
        QuerySnapshot querySnapshot = querySnapshotFuture.get();

         List<CollectionListDTO> collections = querySnapshot.getDocuments().stream()
                .map(document->{
                Collection collection = document.toObject(Collection.class);
                ModelMapper modelMapper = new ModelMapper();
                CollectionListDTO listDTO = modelMapper.map(collection, CollectionListDTO.class);
                listDTO.setCollectionId(document.getId());

                try {
                    String encodedURL = URLEncoder.encode(listDTO.getThumbnailPath(), "UTF-8");
                    listDTO.setThumbnailPath("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/" + encodedURL + "?alt=media&token=");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                return listDTO;
            }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("dto", collections);
        return response;
    }
    //컬렉션 작성
    public ResponseEntity<String> createCollection(CollectionInputDTO collectionInputDTO) throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //도큐먼트 아이디로 유저 컬렉션 정보 가져옴
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        //컬렉션들
        CollectionReference collectionRef = firestore.collection("Collections");


        //유저의 컬렉션카운트 필드 값 가져옴
        Long collectionCnt = userDocRef.get().get().getLong("collectionCnt");
        //컬렉션 카운트 값 증가해서 넣기
        Map<String, Object> updates = new HashMap<>();
        updates.put("collectionCnt", (Long)collectionCnt+1);
        userDocRef.update(updates);
        long newCollectionCnt = collectionCnt+1;

        ModelMapper modelMapper = new ModelMapper();
        Collection collection = modelMapper.map(collectionInputDTO, Collection.class);
        //작성자 아이디 셋
        collection.setUserId(userDocumentId);
        //작성하는 컬렉션의 컬렉션 아이디 만들어줌
        String newCollectionId = userDetails.getUsername() + "-" + newCollectionCnt;
        List<String> imageUrls = saveImagesToStorage(newCollectionId, collectionInputDTO.getFilePath());
        //사진들 저장
        collection.setFilePath(imageUrls);
        collection.setThumbnailPath(imageUrls.get(0));
        //라이크카운트
        collection.setLikeCount(0);
        //작성날짜 셋
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = now.atZone(zoneId).toInstant();
        Date date = Date.from(instant);
        Timestamp firestoreTimestamp = Timestamp.of(date);
        Date createdDate = firestoreTimestamp.toDate();
        collection.setCreatedDate(createdDate);

        //컬렉션테이블에 추가
        DocumentReference docRef = collectionRef.add(collection).get();

        // 방금 저장한 도큐먼트의 아이디 가져오기
        String documentId = docRef.getId();
        //유저의 컬렉션 아이디 필드에 추가해주기
        List<Object> userCollectionId = (List<Object>) userDocRef.get().get().get("userCollectionId");
        userCollectionId.add(documentId);

        Map<String, Object> collectionIdAdd = new HashMap<>();
        collectionIdAdd.put("userCollectionId", userCollectionId);
        userDocRef.update(collectionIdAdd);

        return ResponseEntity.ok("잘 됨!");
    }
    //이미지 저장
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
   //컬렉션 삭제
    public ResponseEntity<String> deleteMyCollection(String collectionId) throws Exception{
        CollectionReference collectionRef = firestore.collection("Collections");
        ApiFuture<WriteResult> deleteApiFuture = collectionRef.document(collectionId).delete();

        //도큐먼트 아이디로 유저 컬렉션 정보 가져옴
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);

        List<Object> userCollectionId = (List<Object>) userDocRef.get().get().get("userCollectionId");
        userCollectionId.remove(collectionId);

        // 업데이트된 likes 배열을 저장
        Map<String, Object> updates = new HashMap<>();
        updates.put("likes", userCollectionId);
        userDocRef.update(updates);

        return ResponseEntity.ok("삭제 성공");
    }
    //컬렉션 검색
    public Map<String, Object>  searchCollection(String hashTag1, String hashTag2, String hashTag3) throws Exception{
        String userDocumentId = userService.loginUserDocumentId();
        CollectionReference collectionRef = firestore.collection("Collections");

        Set<String> documentIds = new HashSet<>(); // 얘는 중복 허용 안함 & 해시태그 일치하는 도큐먼트 아이디 모을 리스트

        // 해시태그1에 대한 쿼리
        QuerySnapshot querySnapshot1 = collectionRef.whereArrayContains("hashTags", hashTag1).get().get();
        for (DocumentSnapshot document : querySnapshot1.getDocuments()) {
            documentIds.add(document.getId());
        }

        // 해시태그2 있으면 쿼리 실행
        if (hashTag2 != null && !hashTag2.trim().isEmpty()) {
            QuerySnapshot querySnapshot2 = collectionRef.whereArrayContains("hashTags", hashTag2).get().get();
            for (DocumentSnapshot document : querySnapshot2.getDocuments()) {
                documentIds.add(document.getId());
            }
        }

        // 해시태그3이 있으면 쿼리 실행
        if (hashTag3 != null && !hashTag3.trim().isEmpty()) {
            QuerySnapshot querySnapshot3 = collectionRef.whereArrayContains("hashTags", hashTag3).get().get();
            for (DocumentSnapshot document : querySnapshot3.getDocuments()) {
                documentIds.add(document.getId());
            }
        }

        //가져온 도큐먼트 아이디들과 일치하는 컬렉션들 정보 가져옴
        List<CollectionListDTO> collections = new ArrayList<>();
        for (String documentId : documentIds) {
            // 문서 ID로 문서 정보 조회
            DocumentSnapshot document = collectionRef.document(documentId).get().get();
            if (document.exists()) {
                ModelMapper modelMapper = new ModelMapper();
                Collection collection = document.toObject(Collection.class);
                CollectionListDTO listDTO = modelMapper.map(collection, CollectionListDTO.class);
                listDTO.setCollectionId(document.getId());

                // 썸네일 URL 인코딩
                try {
                    String encodedURL = URLEncoder.encode(listDTO.getThumbnailPath(), "UTF-8");
                    listDTO.setThumbnailPath("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/" + encodedURL + "?alt=media&token=");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                collections.add(listDTO);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("collections", collections);
        return response;
    }
    //컬렉션 팔아주세요 등록
    public String addCollectionLike(String documentId)throws ExecutionException,InterruptedException{
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);

        List<String> userCollectionLikes = (List<String>) userDocRef.get().get().get("collectionLikes");
        userCollectionLikes.add(documentId);
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("collectionLikes", userCollectionLikes);
        userDocRef.update(userUpdates);


        //컬렉션 정보 가져오기
        DocumentReference collectionDocRef = firestore.collection("Collections").document(documentId);
        //컬렉션의 좋아요 개수 가져오기
        Long collectionLikeCnt = (Long) collectionDocRef.get().get().getLong("likeCount");
        //좋아요 개수 증가해서 넣어주기
        Map<String, Object> likesUpdate = new HashMap<>();
        likesUpdate.put("likeCount", (Long)collectionLikeCnt+1);
        collectionDocRef.update(likesUpdate);
        String updateCnt = String.valueOf(collectionLikeCnt+1);     //우선 ㅜㅅㅜ


        return updateCnt;

    }
    //컬렉션 팔아주세요 취소
    public String removeCollectionLike(String documentId)throws ExecutionException,InterruptedException{
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);

        //likes 필드 배열
        DocumentSnapshot userDocSnapshot = userDocRef.get().get();
        List<Object> likes = (List<Object>) userDocSnapshot.get("collectionLikes");

        likes.remove(documentId);

        // 업데이트된 likes 배열을 저장
        Map<String, Object> updates = new HashMap<>();
        updates.put("collectionLikes", likes);
        userDocRef.update(updates);

        DocumentReference collectionDocRef = firestore.collection("Collections").document(documentId);

        //컬렉션의 좋아요 개수 가져오기
        Long collectionLikeCnt = (Long) collectionDocRef.get().get().getLong("likeCount");

        //좋아요 개수 증가해서 넣어주기
        Map<String, Object> likesUpdate = new HashMap<>();
        likesUpdate.put("likeCount", (Long)collectionLikeCnt-1);
        collectionDocRef.update(likesUpdate);

        String updateCnt = String.valueOf(collectionLikeCnt-1);

        return updateCnt;

    }

}
