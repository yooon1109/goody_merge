package com.honeybee.goody.Post;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private PostService postService;

    @Operation(summary = "판매해요 게시글", description = "")
    @GetMapping("/sell")
    public ResponseEntity<List<Post>> sellpost(){

        return ResponseEntity.ok();
    }
    
}
