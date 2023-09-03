package com.honeybee.goody.Post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 미리보기", description = "판매해요,구해요,같이해요 게시글 미리보기")
    @GetMapping("/preview-info")
    public ResponseEntity<List<PreviewDTO>> postPreview(@Parameter(description = "판매해요,구해요,같이해요") @RequestParam String postType)
        throws ExecutionException, InterruptedException {
        List<PreviewDTO> posts = null;
        if(postType.equals("판매해요")){
            posts = postService.getPreviewPosts("판매해요");
        } else if (postType.equals("구해요")) {
            posts = postService.getPreviewPosts("구해요");
        } else if (postType.equals("같이해요")) {
            posts = postService.getPreviewPosts("같이해요");
        }else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(posts);
    }


    
}
