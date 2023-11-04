package com.honeybee.goody.User.Review;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 키워드 저장", description = "선택된 키워드들을 리스트로 전송")
    @PostMapping("/keywords")
    public ResponseEntity<String> saveReviewKeywords(@RequestBody Review review){
        reviewService.saveReviewKeywords(review);
        return null;
    }
}
