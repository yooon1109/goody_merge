package com.honeybee.goody.Post;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "판매해요 게시글", description = "판매해요 게시글 미리보기")
    @GetMapping("/sell")
    public ResponseEntity<List<PreviewDTO>> sellPost() throws ExecutionException, InterruptedException {
       List<PreviewDTO> sellposts =  postService.getPreviewPosts("판매해요");
       return ResponseEntity.ok(sellposts);
    }

    @Operation(summary = "구해요 게시글", description = "구해요 게시글 미리보기")
    @GetMapping("/need")
    public ResponseEntity<List<PreviewDTO>> needPost() throws ExecutionException, InterruptedException {
        List<PreviewDTO> needPosts =  postService.getPreviewPosts("구해요");
        return ResponseEntity.ok(needPosts);
    }
    
}
