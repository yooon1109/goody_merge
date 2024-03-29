package com.honeybee.goody.Contents;

import com.google.cloud.firestore.WriteResult;
import com.google.firestore.v1.WriteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
        Map<String,Object> posts = contentsService.getPreviewContents(transType,page);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "게시글 검색", description = "파라미터값에 따라 검색")
    @GetMapping( "/search")
    public ResponseEntity<List<PreviewDTO>> contentsSearch( @Parameter(description = "해시태그 검색") @RequestParam(required = false) String search,
                                                            @Parameter(description = "") @RequestParam(required = false) String category,
                                                            @Parameter(description = "") @RequestParam(required = false) String transType,
                                                            @Parameter(description = "") @RequestParam(required = false) Boolean sold)
            throws ExecutionException, InterruptedException {

        List<PreviewDTO> list = contentsService.SearchPreviewContents(search,category,transType,sold);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "게시글 상세정보", description = "게시글 문서아이디로 상세정보 반환")
    @GetMapping("/detail")
    public ResponseEntity<ContentsDetailDTO> contentDetail(@Parameter(description = "게시글 documentId") @RequestParam String documentId)
            throws Exception {
        ContentsDetailDTO contentsDetailDTO = contentsService.getContentsDetail(documentId);
        return ResponseEntity.ok(contentsDetailDTO);
    }

    @Operation(summary = "게시글 작성", description = "게시글 작성하기")
    @PostMapping(value = "/",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postDetail(@ModelAttribute ContentsInsertDTO contentsInsertDTO)
        throws ExecutionException, InterruptedException {
        String postId = contentsService.setContents(contentsInsertDTO);
        return ResponseEntity.ok(postId);
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제하기")
    @DeleteMapping(value = "delete")
    public ResponseEntity<String> contentDelete(@RequestParam String documentId)
            throws Exception{
        return ResponseEntity.ok(contentsService.deleteContents(documentId));
    }

    @Operation(summary = "게시글 좋아요", description = "게시글 좋아요 버튼")
    @PostMapping("/addlike")
    public ResponseEntity<String> contentsLikes(@Parameter(description = "게시글 documentId") @RequestParam String documentId)
            throws ExecutionException, InterruptedException {

        String id = contentsService.addContentsLike(documentId);
        return ResponseEntity.ok(id);
    }

    @Operation(summary = "게시글 좋아요 취소", description = "게시글 좋아요 취소")
    @PostMapping("/removeLike")
    public ResponseEntity<String> removeLike(@Parameter(description = "게시글 documentId") @RequestParam String documentId)
            throws ExecutionException, InterruptedException {

        String id = contentsService.removeContentsLike(documentId);
        return ResponseEntity.ok(id);
    }


}
