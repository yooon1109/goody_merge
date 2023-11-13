package com.honeybee.goody.Collection;

import com.honeybee.goody.MyPage.MyPageUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("collection")
public class CollectionController {
    private final CollectionService collectionService;

    @Operation(summary = "컬렉션 상세페이지", description = "유저의 컬렉션 상세페이지")
    @GetMapping("/detail")
    public ResponseEntity<CollectionDetailDTO> getCollectionDetail(@Parameter(description = "컬렉션 번호") @RequestParam String collectionId) throws Exception {
            CollectionDetailDTO collectionDetailDTO = collectionService.getCollectionDetail(collectionId);
            return ResponseEntity.ok(collectionDetailDTO);
    }

    @Operation(summary = "컬렉션 목록페이지", description = "유저의 컬렉션 목록페이지")
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getCollectionList() throws Exception {
        Map<String,Object> contents = collectionService.getCollectionList();
        return ResponseEntity.ok(contents);
    }

    @Operation(summary = "컬렉션 글쓰기", description = "유저의 컬렉션 글쓰기페이지")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createCollection(@ModelAttribute CollectionInputDTO inputData) throws Exception {
        return collectionService.createCollection(inputData);
    }

    @Operation(summary = "컬렉션 삭제", description = "유저의 컬렉션 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCollection(@Parameter(description = "컬렉션 삭제") @RequestParam String collectionId) throws Exception {
        return collectionService.deleteMyCollection(collectionId);
    }

    @Operation(summary = "컬렉션 해쉬태그 검색")
    @GetMapping( "/search")
    public ResponseEntity<Map<String, Object>> collectionSearch(@Parameter(description = "해쉬태그1") @RequestParam String hashTag1,
                                                                @Parameter(description = "해쉬태그2") @RequestParam(required = false) String hashTag2,
                                                                @Parameter(description = "해쉬태그3") @RequestParam(required = false) String hashTag3) throws Exception {

        Map<String,Object> tags = collectionService.searchCollection(hashTag1,hashTag2,hashTag3);
        return ResponseEntity.ok(tags);
    }

    @Operation(summary = "컬렉션 팔아주세요", description = "컬렉션 팔아주세요 버튼")
    @PostMapping("/addLike")
    public ResponseEntity<String> collectionLikes(@Parameter(description = "게시글 documentId") @RequestParam String documentId)
            throws ExecutionException, InterruptedException {

        String id = collectionService.addCollectionLike(documentId);
        return ResponseEntity.ok(id);
    }

    @Operation(summary = "컬렉션 팔아주세요 취소", description = "컬렉션 팔아주세요 취소")
    @PostMapping("/removeLike")
    public ResponseEntity<String> removeLike(@Parameter(description = "게시글 documentId") @RequestParam String documentId)
            throws ExecutionException, InterruptedException {

        String id = collectionService.removeCollectionLike(documentId);
        return ResponseEntity.ok(id);
    }


    @Operation(summary = "컬렉션 정보 수정", description = "유저의 컬렉션 수정")
    @PatchMapping("/update")
    public ResponseEntity<String> updateCollection(@RequestBody CollectionUpdateDTO updateDTO, @RequestParam String documentId) throws Exception {

        String update =  collectionService.updateCollection(updateDTO, documentId);
        return ResponseEntity.ok(update);
    }
}
