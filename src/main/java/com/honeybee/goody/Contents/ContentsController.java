package com.honeybee.goody.Contents;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("contents")
@RequiredArgsConstructor
public class ContentsController {

    private final ContentsService contentsService;

    @Operation(summary = "게시글 미리보기", description = "판매해요,구해요,같이해요 게시글 미리보기")
    @GetMapping("/preview-info")
    public ResponseEntity<Map<String,Object>> postPreview(@Parameter(description = "판매해요,구해요,같이해요") @RequestParam String transType,
                                                          @Parameter(description = "페이지") @RequestParam int page)
        throws ExecutionException, InterruptedException {
        Map<String,Object> posts = new HashMap<>();
        if(transType.equals("판매해요")){
            posts = contentsService.getPreviewContents("판매해요",page);
        } else if (transType.equals("구해요")) {
            posts = contentsService.getPreviewContents("구해요",page);
        } else if (transType.equals("같이해요")) {
            posts = contentsService.getPreviewContents("같이해요",page);
        }else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "게시글 미리보기", description = "판매해요,구해요,같이해요 게시글 미리보기")
    @GetMapping( "/search")
    public ResponseEntity<List<PreviewDTO>> contentsSearch(@Parameter(description = "검색어") @RequestParam(required = false) String search,
                                                       @Parameter(description = "") @RequestParam(required = false) String category,
                                                       @Parameter(description = "") @RequestParam(required = false) String transType,
                                                       @Parameter(description = "") @RequestParam(required = false) Boolean sold)
            throws ExecutionException, InterruptedException {

        List<PreviewDTO> list = contentsService.SearchPreviewContents(search,category,transType,sold);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "게시글 작성", description = "게시글 작성하기")
    @PostMapping(value = "/",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postDetail(@ModelAttribute ContentsDTO contentsDTO)
        throws ExecutionException, InterruptedException {
        String postId = contentsService.setContents(contentsDTO);
        return ResponseEntity.ok(postId);
    }
}
