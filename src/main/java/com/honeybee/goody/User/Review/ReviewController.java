package com.honeybee.goody.User.Review;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 키워드 저장", description = "선택된 키워드들을 리스트로 전송")
    @PostMapping("/keywords")
    public ResponseEntity<String> saveReviewKeywords(@RequestBody ReviewReceiveDTO review
                                                    ,@Parameter(description = "리뷰를 받는 사람 아이디") @RequestParam String receiveId, @RequestParam String contentsDocumentId)
        throws Exception{
        String documentId = reviewService.saveReviewKeywords(review,receiveId,contentsDocumentId);
        return ResponseEntity.ok(documentId);
    }

    @Operation(summary = "리뷰 별점 저장", description = "이전에 키워드 저장하고 받은 문서아이디값으로 전송")
    @PostMapping("/rate")
    public ResponseEntity<Map<String,Object>> saveReviewRate(@Parameter(description = "키워드저장하고 받은 문서아이디")@RequestParam String reviewDocumentId,
                                                             @Parameter(description = "리뷰를 받는 사람")@RequestParam String receiveId,
                                                             @Parameter(description = "별점")@RequestParam Long rate) throws Exception{
        Map<String,Object> response = reviewService.saveReviewRate(reviewDocumentId,receiveId,rate);
        return ResponseEntity.ok(response);
    }
}
