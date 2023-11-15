package com.honeybee.goody.MyPage;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("myPage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "마이페이지", description = "유저의 마이페이지 첫 화면")
    @GetMapping("/")
    public ResponseEntity<MyPageHomeDTO> getMyPage() throws Exception {

        MyPageHomeDTO dto = myPageService.getMyPageHome();
        return ResponseEntity.ok(dto);

    }

    @Operation(summary = "마이페이지 게시글 찜목록", description = "유저의 마이페이지 게시글 찜 목록 리스트")
    @GetMapping("/contentsLikeList")
    public ResponseEntity<Map<String,Object>> getContentsLikePreview() throws Exception {

        Map<String,Object> contents = myPageService.getContentsLikePreview();
        return ResponseEntity.ok(contents);
    }

    @Operation(summary = "마이페이지 컬렉션 팔아주세요 목록", description = "유저의 마이페이지 컬렉션 팔아주세요 목록 리스트")
    @GetMapping("/collectionLikeList")
    public ResponseEntity<Map<String,Object>> getCollectionLikePreview() throws Exception {
        Map<String,Object> contents = myPageService.getCollectionLikePreview();
        return ResponseEntity.ok(contents);
    }

    @Operation(summary = "마이페이지 내 게시글 목록", description = "유저의 마이페이지 내 게시글 목록 리스트")
    @GetMapping("/myContentsList")
    public ResponseEntity<Map<String,Object>> getMyContentsPreview() throws Exception {

        Map<String,Object> contents = myPageService.getMyContentsList();
        return ResponseEntity.ok(contents);
    }
    @Operation(summary = "마이페이지 내 정보 수정", description = "유저의 마이페이지 수정")
    @PatchMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestBody MyPageUpdateDTO updateDTO) throws Exception {

        String update =  myPageService.updateUserInfo(updateDTO);;
        return ResponseEntity.ok(update);
    }

    @Operation(summary = "마이페이지 리뷰 목록", description = "유저의 마이페이지 내 리뷰 목록 리스트")
    @GetMapping("/myReviewList")
    public ResponseEntity<MyPageReviewDTO> getMyReview() throws Exception{

        MyPageReviewDTO dto = myPageService.getMyReviewList();
        return ResponseEntity.ok(dto);
    }
}
