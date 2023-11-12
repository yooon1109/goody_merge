package com.honeybee.goody.MyPage;

import com.google.cloud.firestore.*;
import com.honeybee.goody.Collection.Collection;
import com.honeybee.goody.Collection.CollectionListDTO;
import com.honeybee.goody.Contents.Contents;
import com.honeybee.goody.Contents.PreviewDTO;
import com.honeybee.goody.User.UserService;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

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
            //가져온 도큐먼트 아이디들과 일치하는 컬렉션들 정보 가져옴
            List<CollectionListDTO> collections = new ArrayList<>();

            for (String like : likes) {
                try {
                    // 문서 ID로 문서 정보 조회
                    DocumentSnapshot document = contentsRef.document(like).get().get();
                    ModelMapper modelMapper = new ModelMapper();
                    Collection collection = document.toObject(Collection.class);
                    CollectionListDTO dto = modelMapper.map(collection, CollectionListDTO.class);

                    if (document.exists()) {
                        dto.setDocumentId(document.getId());
                        // 썸네일 URL 인코딩
                        try {
                            String encodedURL = URLEncoder.encode(dto.getThumbnailPath(), "UTF-8");
                            dto.setThumbnailPath("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/" + encodedURL + "?alt=media&token=");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        collections.add(dto);
                    }
                }catch (InterruptedException | ExecutionException e) {}
            }
            result.put("collections", collections);
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

}
