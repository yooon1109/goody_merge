package com.honeybee.goody.MyPage;

import com.google.cloud.firestore.*;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.honeybee.goody.Collection.Collection;
import com.honeybee.goody.Collection.CollectionListDTO;
import com.honeybee.goody.Contents.Contents;
import com.honeybee.goody.Contents.PreviewDTO;
import com.honeybee.goody.User.UserService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class MyPageService {
    private final Firestore firestore;
    private final UserService userService;


    public MyPageHomeDTO getMyPageHome() throws Exception{
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        DocumentSnapshot userDocSnapshot = userDocRef.get().get();

        if (userDocSnapshot.exists()) {
            MyPage myPage = userDocSnapshot.toObject(MyPage.class);
            ModelMapper modelMapper = new ModelMapper();
            MyPageHomeDTO dto = modelMapper.map(myPage, MyPageHomeDTO.class);

            // createdDate 필드를 매핑
            com.google.cloud.Timestamp firestoreTimestamp = userDocSnapshot.get("joinDate", com.google.cloud.Timestamp.class);
            java.util.Date joinDate = firestoreTimestamp.toDate();
            dto.setJoinDate(joinDate);

            // 현재 시간 계산
            Date currentDate = new Date();
            long diffInMillies = currentDate.getTime() - joinDate.getTime();
            long daysSinceJoin = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)+1;
            dto.setDaysSinceJoin(daysSinceJoin);


            String profileImg = dto.getProfileImg();
            if(profileImg.equals("Null")) {
                dto.setProfileImg("Null");
            }
            else{
                try{
                    String encodedURL = URLEncoder.encode(profileImg, "UTF-8");
                    String profileImgPath = "https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/"+ encodedURL + "?alt=media&token=";
                    dto.setProfileImg(profileImgPath);
                } catch(UnsupportedEncodingException e){throw new Exception(e);}
            }
            return dto;
        } else {
            throw new Exception("없음!!");
        }
    }

    public Map<String,Object> getContentsLikePreview() throws Exception {
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        DocumentSnapshot userDocSnapshot = userDocRef.get().get();

        CollectionReference contentsRef = firestore.collection("Contents");

        List<String> likes = (List<String>) userDocSnapshot.get("contentsLikes");
        Map<String, Object> result = new HashMap<>();


        if (likes == null || likes.isEmpty()) {
            result.put("dto", "Null");
        }
        else {
            likes = new ArrayList<>(new HashSet<>(likes));//중복제거
            //가져온 도큐먼트 아이디들과 일치하는 컬렉션들 정보 가져옴
            List<PreviewDTO> contents = new ArrayList<>();
            List<String> likesToRemove = new ArrayList<>();//리스트 중에서 존재하지 않는 문서 아이디 리스트
            for (String like : likes) {
                try {
                    DocumentSnapshot document = contentsRef.document(like).get().get();
                    if (document.exists()) {
                        ModelMapper modelMapper = new ModelMapper();
                        Contents content = document.toObject(Contents.class);
                        PreviewDTO previewDTO = modelMapper.map(content, PreviewDTO.class);
                        previewDTO.setDocumentId(document.getId());

                        try {
                            String encodedURL = URLEncoder.encode(previewDTO.getThumbnailImg(), "UTF-8");
                            previewDTO.setThumbnailImg("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/" + encodedURL + "?alt=media&token=");
                        } catch (UnsupportedEncodingException e) {
                            // URL 인코딩 예외 처리
                            throw new RuntimeException(e);
                        }
                        contents.add(previewDTO);
                    }else{//존재하지 않으면 삭제할 리스트에 추가
                        likesToRemove.add(like);
                    }
                }catch (InterruptedException | ExecutionException e){}
            }
            likes.removeAll(likesToRemove);//유저의 좋아요 리스트에서 존재하지않는 문서아이디들 삭제
            userDocRef.update("contentsLikes",likes);//해당 필드 업데이트
            result.put("dto", contents);
        }
        return result;
    }

    public Map<String,Object> getCollectionLikePreview() throws Exception {
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        DocumentSnapshot userDocSnapshot = userDocRef.get().get();

        CollectionReference contentsRef = firestore.collection("Collections");

        List<String> likes = (List<String>) userDocSnapshot.get("collectionLikes");

        Map<String, Object> result = new HashMap<>();

        if (likes == null || likes.isEmpty()) {
            result.put("collectionLikes", "Null");
        }
        else {
            likes = new ArrayList<>(new HashSet<>(likes));//중복제거
            //가져온 도큐먼트 아이디들과 일치하는 컬렉션들 정보 가져옴
            List<CollectionListDTO> collections = new ArrayList<>();
            List<String> likesToRemove = new ArrayList<>();//리스트 중에서 존재하지 않는 문서 아이디 리스트
            for (String like : likes) {
                try {
                    // 문서 ID로 문서 정보 조회
                    DocumentSnapshot document = contentsRef.document(like).get().get();

                    if (document.exists()) {//존재하면
                        ModelMapper modelMapper = new ModelMapper();
                        Collection collection = document.toObject(Collection.class);
                        CollectionListDTO dto = modelMapper.map(collection, CollectionListDTO.class);
                        dto.setDocumentId(document.getId());
                        // 썸네일 URL 인코딩
                        try {
                            String encodedURL = URLEncoder.encode(dto.getThumbnailPath(), "UTF-8");
                            dto.setThumbnailPath("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/" + encodedURL + "?alt=media&token=");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        collections.add(dto);
                    }else{//존재하지 않으면 삭제할 리스트에 추가
                        likesToRemove.add(like);
                    }
                }catch (InterruptedException | ExecutionException e) {}
            }
            likes.removeAll(likesToRemove);//유저의 좋아요 리스트에서 존재하지않는 문서아이디들 삭제
            userDocRef.update("collectionLikes",likes);//해당 필드 업데이트
            result.put("collectionLikes", collections);
        }
        return result;
    }

    public Map<String,Object> getMyContentsList() throws Exception {
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        DocumentSnapshot userDocSnapshot = userDocRef.get().get();

        CollectionReference contentsRef = firestore.collection("Contents");

        List<String> likes = (List<String>) userDocSnapshot.get("userContentsId");
        Map<String, Object> result = new HashMap<>();


        if (likes == null || likes.isEmpty()) {
            result.put("userContentsId", "Null");
        }
        else {
            //가져온 도큐먼트 아이디들과 일치하는 컬렉션들 정보 가져옴
            List<PreviewDTO> contents = new ArrayList<>();

            for (String like : likes) {
                try {
                    DocumentSnapshot document = contentsRef.document(like).get().get();
                    if (document.exists()) {
                        ModelMapper modelMapper = new ModelMapper();
                        Contents content = document.toObject(Contents.class);
                        PreviewDTO previewDTO = modelMapper.map(content, PreviewDTO.class);
                        previewDTO.setDocumentId(document.getId());

                        try {
                            String encodedURL = URLEncoder.encode(previewDTO.getThumbnailImg(), "UTF-8");
                            previewDTO.setThumbnailImg("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/" + encodedURL + "?alt=media&token=");
                        } catch (UnsupportedEncodingException e) {
                            // URL 인코딩 예외 처리
                            throw new RuntimeException(e);
                        }
                        contents.add(previewDTO);
                    }
                }catch (InterruptedException | ExecutionException e){}
            }

            result.put("dto", contents);
        }
        return result;
    }

    public  String updateUserInfo(MyPageUpdateDTO updateDTO) throws  Exception{
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();

        if (updateDTO.getNickname() != null) {
            userDocRef.update("nickname", updateDTO.getNickname());
        }
        if (updateDTO.getUserPhoneNum() != null) {
            userDocRef.update("userPhoneNum", updateDTO.getUserPhoneNum());
        }
        if (updateDTO.getAccountBank() != null) {
            userDocRef.update("accountBank", updateDTO.getAccountBank());
        }
        if (updateDTO.getAccountNum() != null) {
            userDocRef.update("accountNum", updateDTO.getAccountNum());
        }
        if (updateDTO.getAddress() != null) {
            userDocRef.update("address", updateDTO.getAddress());
        }

        if (updateDTO.getProfileImg() != null) {
            try {
                String imageUrl = saveImagesToStorage(userName, updateDTO.getProfileImg());
                userDocRef.update("profileImg", imageUrl);
            } catch (IOException e) {e.printStackTrace();}
        }

        return userDocumentId+"업데이트 성공";
    }

    private String saveImagesToStorage(String userName, MultipartFile image) throws IOException {
        String imageUrl = new String();
        try {
            // Firebase Storage 초기화
            Storage storage = StorageOptions.getDefaultInstance().getService();

            String bucketName = FirebaseApp.getInstance().getOptions().getStorageBucket();
            Bucket bucket = StorageClient.getInstance().bucket(bucketName);//'gs://goody-4b16e.appspot.com'
            InputStream content = new ByteArrayInputStream(image.getBytes());
            Blob blob = bucket.create("profile/" + userName, content,image.getContentType());
            imageUrl = blob.getName();

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }
        return imageUrl;
    }

    public MyPageReviewDTO getMyReviewList() throws Exception{

        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        DocumentSnapshot userDocSnapshot = userDocRef.get().get();

        if (userDocSnapshot.exists()) {
            MyPage myPage = userDocSnapshot.toObject(MyPage.class);
            ModelMapper modelMapper = new ModelMapper();
            MyPageReviewDTO dto = modelMapper.map(myPage, MyPageReviewDTO.class);

            return dto;
        } else {
            throw new Exception("오류 발생!!");
        }
    }
}
