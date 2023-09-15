package com.honeybee.goody.File;

import com.honeybee.goody.Contents.ContentsService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final ContentsService postService;
    @Operation(summary = "파일 불러오기", description = "해당하는 이름의 파일.")
    @GetMapping("/files/")
    public ResponseEntity<byte[]> getFile(@RequestParam String file) throws IOException {
        byte[] fileContent = fileService.getFileContent(file);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        //headers.setContentDisposition(ContentDisposition.builder("attachment").filename(fileName).build());//파일 다운로드
        return ResponseEntity.ok()
            .headers(headers)
            .body(fileContent);

    }

    @Operation(summary = "파일 업로드", description = "해당 게시글의 파일명과 파일경로")
    @PostMapping(value = "/files",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart MultipartFile multipartFile) throws IOException {
        String filePath = fileService.fileUpload(multipartFile);
        return ResponseEntity.ok(filePath);
    }

}
