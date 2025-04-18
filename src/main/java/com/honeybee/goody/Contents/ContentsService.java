package com.honeybee.goody.Contents;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.Query.Direction;
import com.honeybee.goody.File.FileService;
import com.honeybee.goody.User.UserService;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentsService {
    private final Firestore firestore;
    private final FileService fileService;
    private final UserService userService;

    //컨텐츠 미리보기
    public Map<String,Object> getPreviewContents(String type,int page) throws ExecutionException, InterruptedException {
        //컬렉션참조
        CollectionReference collectionRef = firestore.collection("Contents");//필드를 기준으로 내림차순 정렬

        Query query = collectionRef.orderBy("createdDate", Direction.DESCENDING);

        int pageSize = 5; // 페이지 크기

        ApiFuture<QuerySnapshot> querySnapshotFuture = query.whereEqualTo("transType", type)
                                                                    .limit(pageSize+1).offset(page * 5).get();
        QuerySnapshot querySnapshot = querySnapshotFuture.get();
        List<PreviewDTO> posts = querySnapshot.getDocuments().stream()
            .map(doc -> {
                Contents contents = doc.toObject(Contents.class);
                ModelMapper modelMapper = new ModelMapper();
                PreviewDTO previewDTO = modelMapper.map(contents, PreviewDTO.class);
                previewDTO.setDocumentId(doc.getId()); // 문서의 ID를 설정
                try {
                    String encodedURL = URLEncoder.encode(previewDTO.getThumbnailImg(), "UTF-8");
                    previewDTO.setThumbnailImg("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/"+encodedURL + "?alt=media&token=");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

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

    //컨텐츠 검색
    public List<PreviewDTO> SearchPreviewContents(String search,String category,String transType, Boolean sold) throws ExecutionException, InterruptedException {
        CollectionReference collectionRef = firestore.collection("Contents");

        Query query = collectionRef.orderBy("createdDate", Direction.DESCENDING);

        if(search!=null){
            // 'title' 필드 또는 'explain' 필드에서 검색 후 정렬->title 완전 일치만 검색 가능
            query = query
                    .whereArrayContains("hashTags",search);
                   // .whereGreaterThanOrEqualTo("title", search);

        }
        if(category!=null){
            query = query
                    .whereEqualTo("category",category);
        }
        if(transType!=null){
            query = query
                    .whereEqualTo("transType",transType);
        }
        if(sold!=null){
            query = query
                    .whereEqualTo("sold",sold);
        }
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<PreviewDTO> contents = querySnapshot.get().getDocuments().stream()
                .map(document->{
                    Contents con = document.toObject(Contents.class);
                    ModelMapper modelMapper = new ModelMapper();
                    PreviewDTO previewDTO = modelMapper.map(con, PreviewDTO.class);
                    previewDTO.setDocumentId(document.getId());
                    try {
                        String encodedURL = URLEncoder.encode(previewDTO.getThumbnailImg(), "UTF-8");
                        previewDTO.setThumbnailImg("https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/"+encodedURL + "?alt=media&token=");
                        return previewDTO;
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        //총개수 추가하기..?

        return contents;

    }

    //컨텐츠 상세 정보
    public ContentsDetailDTO getContentsDetail(String documentId) throws Exception {
        //컨텐츠 정보
        CollectionReference collectionRef = firestore.collection("Contents");
        DocumentSnapshot documentSnapshot = collectionRef.document(documentId).get().get();
        //유저정보
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);


        if(documentSnapshot.exists()){
            Contents contents = documentSnapshot.toObject(Contents.class);
            ModelMapper modelMapper = new ModelMapper();
            ContentsDetailDTO contentsDetailDTO = modelMapper.map(contents, ContentsDetailDTO.class);
            contentsDetailDTO.setLike(false);
            List<String> imgPathList = contentsDetailDTO.getImgPath().stream().map(img->{
                try {
                    String encodedURL = URLEncoder.encode(img, "UTF-8");

                    return "https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/"+ encodedURL + "?alt=media&token=";
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }).toList();
            contentsDetailDTO.setImgPath(imgPathList);
            //문서Id로 작성자 아이디찾기
            DocumentSnapshot doc = firestore.collection("Users").document(contents.getWriterId()).get().get();
            contentsDetailDTO.setWriterDocumentId(contents.getWriterId());
            contentsDetailDTO.setWriterId(doc.getString("userId"));
            contentsDetailDTO.setNickname(doc.getString("nickname"));//작성자 닉넴
            contentsDetailDTO.setWriterGrade(doc.getString("grade"));

            String profileImg = doc.getString("profileImg");
            if(profileImg.equals("Null")) {
                contentsDetailDTO.setProfileImg("Null");
            }
            else{
                try{
                    String encodedURL = URLEncoder.encode(profileImg, "UTF-8");
                    String profileImgPath = "https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/"+ encodedURL + "?alt=media&token=";
                    contentsDetailDTO.setProfileImg(profileImgPath);
                } catch(UnsupportedEncodingException e){throw new Exception(e);}
            }


            // 현재 로그인한 사용자의 정보 가져와서 본인글인지 확인
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();
            if(contentsDetailDTO.getWriterId().equals(username)){
                contentsDetailDTO.setMyContents(true);
            }else{
                contentsDetailDTO.setMyContents(false);
            }
            // 사용자가 좋아요한 글인지 확인
            List<String> likes = (List<String>) userDocRef.get().get().get("contentsLikes");
            if(likes!=null){
                for(String like : likes){
                    if(like.equals(documentId)){
                        contentsDetailDTO.setLike(true);
                    }
                }
            }

            //채팅방 생성된 유저인지 확인
            String chatDocumentId = username + "-" + documentId;
            try {
                DocumentSnapshot chatroomRef = firestore.collection("Chats").document(chatDocumentId).get().get();
                if (chatroomRef.exists()) {
                    contentsDetailDTO.setIsChatEntered(true);
                } else {
                    contentsDetailDTO.setIsChatEntered(false);
                }
            } catch(Exception e) {throw new RuntimeException(e);}

            return contentsDetailDTO;
        }else{
            return null;
        }

    }

    //컨텐츠 등록
    public String setContents(ContentsInsertDTO contentsInsertDTO) throws ExecutionException, InterruptedException{

        ModelMapper modelMapper = new ModelMapper();
        Contents contents = modelMapper.map(contentsInsertDTO, Contents.class);//받은 데이터 매핑
        contents.setImgPath(setContentsFilePath(contentsInsertDTO.getImgPath()));//파일 경로 저장
        contents.setThumbnailImg(contents.getImgPath().get(0));//첫번째 사진 썸네일로 저장
        LocalDateTime localDateTime = LocalDateTime.now();
        // LocalDateTime을 Instant로 변환
        java.time.Instant instant = localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant();
        // Instant를 Date로 변환
        Date createdDate = java.util.Date.from(instant);
        contents.setCreatedDate(createdDate);//게시글 등록 시간
        String userDocumentId = userService.loginUserDocumentId();
        contents.setWriterId(userDocumentId);//로그인한 사람 글쓴이로 저장

        CollectionReference collectionRef =firestore.collection("Contents");//컬렉션참조
        ApiFuture<DocumentReference> result = collectionRef.add(contents);//저장

        //만약 같이해요일 경우
        if(contentsInsertDTO.getTransType().equals("같이해요")){
            DocumentReference contentsDocRef = result.get(); // Contents 문서 참조
            Map<String, Object> togetherData = new HashMap<>();
            if(contentsInsertDTO.getNumOfPeople()!=null){
                togetherData.put("numOfPeople", contentsInsertDTO.getNumOfPeople());
            }else if(contentsInsertDTO.getPeople()!=null){
                togetherData.put("people", contentsInsertDTO.getPeople());
            }
            contentsDocRef.update(togetherData);
        }

        String documentId = result.get().getId(); // 생성된 문서의 ID
//        //유저의 컬렉션 아이디 필드에 추가해주기
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);
//        List<Object> userContentsId = (List<Object>) userDocRef.get().get().get("userContentsId");
//        userContentsId.add(documentId);
//
//        Map<String, Object> contentsIdAdd = new HashMap<>();
//        contentsIdAdd.put("userContentsId", userContentsId);
//        userDocRef.update(contentsIdAdd);

        userDocRef.update("userContentsId",FieldValue.arrayUnion(documentId));

        return result.get().getId();
    }

    //컨텐츠 삭제
    public String deleteContents(String documentId) throws Exception{
        CollectionReference collectionRef = firestore.collection("Contents");//컨텐츠 컬렉션
        ApiFuture<WriteResult> deleteApiFuture = collectionRef.document(documentId).delete();

        //도큐먼트 아이디로 유저 컬렉션 정보 가져옴
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);

        List<Object> userContentsId = (List<Object>) userDocRef.get().get().get("userContentsId");
        userContentsId.remove(documentId);

        // 업데이트된 likes 배열을 저장
        Map<String, Object> updates = new HashMap<>();
        updates.put("userContentsId", userContentsId);
        userDocRef.update(updates);

        return "delete success";
    }

    //컨텐츠 파일 저장 후 저장된 경로 반환
    public List<String> setContentsFilePath(List<MultipartFile> files)
        throws ExecutionException, InterruptedException {
        return files.stream().map(file-> {
                    try {
                        return fileService.fileUpload(file,"contents");//file storage에 저장후 저장 경로 반환
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

        ).toList();
    }

    //좋아요 등록
    public String addContentsLike(String documentId)throws ExecutionException,InterruptedException{
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);

        userDocRef.update("contentsLikes",FieldValue.arrayUnion(documentId));
//        List<Object> likes = (List<Object>) userDocRef.get().get().get("contentsLikes");
//
//        if (likes == null) {
//            likes = new ArrayList<>();
//        }
//
//        likes.add(documentId);

//        Map<String, Object> updates = new HashMap<>();
//        updates.put("contentsLikes", likes);
//        userDocRef.update(updates);

        return documentId;

    }

    //좋아요 취소
    public String removeContentsLike(String documentId)throws ExecutionException,InterruptedException{
        String userDocumentId = userService.loginUserDocumentId();
        DocumentReference userDocRef = firestore.collection("Users").document(userDocumentId);

        //likes 필드 배열
        DocumentSnapshot userDocSnapshot = userDocRef.get().get();
        List<Object> likes = (List<Object>) userDocSnapshot.get("contentsLikes");

        likes.remove(documentId);

        // 업데이트된 likes 배열을 저장
        Map<String, Object> updates = new HashMap<>();
        updates.put("contentsLikes", likes);
        userDocRef.update(updates);

        return documentId;

    }

}
