package com.honeybee.goody.File;

import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.net.URLConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    @Operation(summary = "파일 불러오기", description = "해당하는 이름의 파일.")
    @GetMapping("/files/")
    public ResponseEntity<byte[]> getFile(@RequestParam String file) throws IOException {
        byte[] fileContent = fileService.getFileContent(file);

        HttpHeaders headers = new HttpHeaders();
        String fileExtension = file.substring(file.lastIndexOf('.')).toLowerCase();//확장자 추출
        if(fileExtension.equals(".jpg")||fileExtension.equals(".jpeg")){
            headers.setContentType(MediaType.IMAGE_JPEG);//확장자에 맞춰 헤더 세팅
        }else if(fileExtension.equals(".png")){
            headers.setContentType(MediaType.IMAGE_PNG);
        }else{
            throw new IOException("noooo error");
        }
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