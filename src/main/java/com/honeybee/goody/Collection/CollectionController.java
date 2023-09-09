package com.honeybee.goody.Collection;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("collection")
public class CollectionController {
    private final CollectionService collectionService;

    @Operation(summary = "컬렉션 상세페이지", description = "유저의 컬렉션 상세페이지")
    @GetMapping("/detail")
    public Map<String, Object> getCollectionDetail(@Parameter(description = "유저 아이디") @RequestParam String userId,
                                                          @Parameter(description = "컬렉션 번호") @RequestParam String collectionId) throws Exception {

            return collectionService.getCollectionDetail(userId, collectionId);
    }

    @Operation(summary = "컬렉션 목록페이지", description = "유저의 컬렉션 목록페이지")
    @GetMapping("/list")
    public Map<String, Object> getCollectionInfo(@Parameter(description = "유저 아이디") @RequestParam String userId) throws Exception {
        return collectionService.getCollectionList(userId);
    }
}
