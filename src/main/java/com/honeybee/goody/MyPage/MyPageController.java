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


    @Operation(summary = "마이페이지 찜목록", description = "유저의 마이페이지 찜 목록 리스트")
    @GetMapping("/likesPreview")
    public ResponseEntity<Map<String,Object>> getContentsLikesPreview() throws Exception {

        Map<String,Object> contents = myPageService.getMyPageContentsLikesPreview();

        return ResponseEntity.ok(contents);

    }
}
