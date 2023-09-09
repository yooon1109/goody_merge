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
@RequestMapping("collection")
@RequiredArgsConstructor
public class CollectionGetController {

    private final CollectionGetService collectionGetService;

    @Operation(summary = "컬렉션 상세페이지", description = "유저의 컬렉션 상세페이지")
    @GetMapping("/collectionInfo")

    public Map<String, Object> getCollectionInfo(@Parameter(description = "유저 아이디") @RequestParam String userId,
                                                          @Parameter(description = "컬렉션 번호") @RequestParam String collectionId)
            throws Exception {

        //System.out.println(userId);

            return collectionGetService.getCollectionInfo(userId, collectionId);


    }

}
