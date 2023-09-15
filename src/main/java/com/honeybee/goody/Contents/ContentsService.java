package com.honeybee.goody.Contents;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.honeybee.goody.File.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ContentsService {
    private final Firestore firestore;
    private final FileService fileService;

    @Autowired
    public ContentsService(Firestore firestore, FileService fileService) {
        this.firestore = firestore;
        this.fileService = fileService;
    }

    //컨텐츠 미리보기
    public Map<String,Object> getPreviewContents(String type,int page) throws ExecutionException, InterruptedException {
        //컬렉션참조
        //TODO : Post -> Contents로 수정
        //TODO : postDate -> createdDate
        CollectionReference collectionRef = firestore.collection("Contents");//필드를 기준으로 내림차순 정렬
        Query query = collectionRef.orderBy("createdDate", Query.Direction.ASCENDING);
        int pageSize = 5; // 페이지 크기

        ApiFuture<QuerySnapshot> querySnapshotFuture = query.whereEqualTo("transType", type)
                                                                    .limit(pageSize+1).offset(page * 5).get();
        QuerySnapshot querySnapshot = querySnapshotFuture.get();
        List<PreviewDTO> posts = querySnapshot.getDocuments().stream()
            .map(doc -> {
                PreviewDTO previewDTO = doc.toObject(PreviewDTO.class);
                previewDTO.setDocumentId(doc.getId()); // 문서의 ID를 설정
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

    public Map<String,Object> getPreviewContents(String search,int category,int transType, boolean sold){
        CollectionReference collectionRef = firestore.collection("Contents");
        Query query = collectionRef.orderBy("createdDate", Query.Direction.ASCENDING);//필드를 기준으로 내림차순 정렬
        int pageSize = 12; // 페이지 크기

        return null;
    }

    //컨텐츠 등록
    public String setContents(ContentsDTO contentsDTO) throws ExecutionException, InterruptedException{

        ModelMapper modelMapper = new ModelMapper();
        Contents contents = modelMapper.map(contentsDTO, Contents.class);//받은 데이터 매핑
        contents.setFilePath(setContentsFilePath(contentsDTO.getMultipartFiles()));//파일 경로 저장
        LocalDateTime localDateTime = LocalDateTime.now();
        // LocalDateTime을 Instant로 변환
        java.time.Instant instant = localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant();
        // Instant를 Date로 변환
        Date createdDate = java.util.Date.from(instant);
        contents.setCreatedDate(createdDate);//게시글 등록 시간

        CollectionReference collectionRef =firestore.collection("Contents");//컬렉션참조
        ApiFuture<DocumentReference> result = collectionRef.add(contents);//저장

        //만약 같이해요일 경우
        if(contentsDTO.getTransType().equals("같이해요")){
            DocumentReference contentsDocRef = result.get(); // Contents 문서 참조
            Map<String, Object> togetherData = new HashMap<>();
            if(contentsDTO.getNumOfPeople()!=null){
                togetherData.put("numOfPeople",contentsDTO.getNumOfPeople());
            }else if(contentsDTO.getPeople()!=null){
                togetherData.put("people",contentsDTO.getPeople());
            }
            contentsDocRef.update(togetherData);
        }


        return result.get().getId();
    }

    //컨텐츠 파일 저장 후 저장된 경로 반환
    public List<String> setContentsFilePath(List<MultipartFile> files)
        throws ExecutionException, InterruptedException {
        return files.stream().map(file-> {
                    try {
                        return fileService.fileUpload(file);//file storage에 저장후 저장 경로 반환
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

        ).toList();
    }
}
