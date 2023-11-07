package com.honeybee.goody.MyPage;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.honeybee.goody.Collection.Collection;
import com.honeybee.goody.Collection.CollectionListDTO;
import com.honeybee.goody.Contents.Contents;
import com.honeybee.goody.Contents.PreviewDTO;
import com.honeybee.goody.User.UserService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class MyPageService {
    private final Firestore firestore;
    private final UserService userService;

    @Autowired
    public MyPageService(Firestore firestore, UserService userService) throws ExecutionException, InterruptedException {
        this.firestore = firestore;
        this.userService = userService;
    }

    public MyPageHomeDTO getMyPageHome() throws Exception{
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        DocumentSnapshot userDocSnapshot = userDocRef.get().get();

        ModelMapper modelMapper = new ModelMapper();

        if (userDocSnapshot.exists()) {
            MyPageHomeDTO dto = modelMapper.map(userDocSnapshot.getData(), MyPageHomeDTO.class);
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

    public Map<String,Object> getMyPageContentsLikesPreview() throws Exception {
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
        DocumentSnapshot userDocSnapshot = userDocRef.get().get();

        CollectionReference contentsRef = firestore.collection("Contents");

        List<String> likes = (List<String>) userDocSnapshot.get("contentsLikes");
        ModelMapper modelMapper = new ModelMapper();

        List<LikesPreviewDTO> likesPreviewList = likes.stream()
                .map(Object::toString)
                .map(documentId -> {
                    // Contents 컬렉션에서 documentId와 일치하는 도큐먼트를 찾음
                    DocumentReference contentDocRef = contentsRef.document(documentId);
                    try {
                        if (contentDocRef.get().get().exists()) {
                            // documentId와 일치하는 도큐먼트가 있을 경우, 해당 도큐먼트를 LikesPreviewDTO로 매핑
                            LikesPreviewDTO likesPreviewDTO = modelMapper.map(contentDocRef.get().get().getData(), LikesPreviewDTO.class);
                            likesPreviewDTO.setDocumentId(documentId);

                            try {
                                String encodedURL = URLEncoder.encode(likesPreviewDTO.getThumbnailImg(), "UTF-8");
                                likesPreviewDTO.setThumbnailImg("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/"+encodedURL + "?alt=media&token=");
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                            return likesPreviewDTO;
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    return null; // 일치하는 도큐먼트가 없을 경우 null 반환
                })
                .filter(likesPreviewDTO -> likesPreviewDTO != null) // null 값 필터링
                .collect(Collectors.toList());

        // LikesPreviewDTO 객체 리스트를 맵으로 반환
        Map<String, Object> likesPreview = Map.of("likedPreviews", likesPreviewList);

        return likesPreview;
    }
}
