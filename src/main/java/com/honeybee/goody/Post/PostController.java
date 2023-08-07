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

    @Operation(summary = "판매해요 게시글", description = "")
    @GetMapping("/sell")
    public ResponseEntity<List<Post>> sellPost() throws ExecutionException, InterruptedException {
       List<Post> sellposts =  postService.getPreviewPosts("판매해요");
       return ResponseEntity.ok(sellposts);
    }

    @Operation(summary = "구해요 게시글", description = "")
    @GetMapping("/need")
    public ResponseEntity<List<Post>> needPost() throws ExecutionException, InterruptedException {
        List<Post> needPosts =  postService.getPreviewPosts("구해요");
        return ResponseEntity.ok(needPosts);
    }
    
}
