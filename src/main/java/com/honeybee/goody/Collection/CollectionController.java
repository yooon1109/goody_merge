package com.honeybee.goody.Collection;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.Map;

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
//        return collectionService.getCollectionDetail(collectionId);
            CollectionDetailDTO collectionDetailDTO = collectionService.getCollectionDetail(collectionId);
            return ResponseEntity.ok(collectionDetailDTO);
    }

    @Operation(summary = "컬렉션 목록페이지", description = "유저의 컬렉션 목록페이지")
    @GetMapping("/list")
    public Map<String, Object> getCollectionList() throws Exception {
        return collectionService.getCollectionList();
    }

    @Operation(summary = "컬렉션 글쓰기", description = "유저의 컬렉션 글쓰기페이지")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createCollection(@ModelAttribute CollectionInputDTO inputData) throws Exception {
        return collectionService.createCollection(inputData);
    }
}
