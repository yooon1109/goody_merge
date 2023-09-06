package com.honeybee.goody.File;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class FileService {

    private final Firestore firestore;
    private final Storage storage;

    @Autowired
    public FileService(Firestore firestore, Storage storage) {
        this.firestore = firestore;
        this.storage = storage;
    }

    public String fileUpload(MultipartFile multipartFile)
        throws IOException {

        //String filename = id + "_" +multipartFile.getOriginalFilename();
        String bucketName = FirebaseApp.getInstance().getOptions().getStorageBucket(); // Firebase Storage 버킷 이름
        Bucket bucket = StorageClient.getInstance().bucket(bucketName);//'gs://goody-4b16e.appspot.com'
        InputStream content = new ByteArrayInputStream(multipartFile.getBytes());
        Blob blob = bucket.create("Image/"+multipartFile.getOriginalFilename(),content,multipartFile.getContentType());
        return blob.getName();

    }

    // 파일 내용을 바이트 배열로 가져오는 메서드
    public byte[] getFileContent(String file) throws IOException {

        String bucketName = FirebaseApp.getInstance().getOptions().getStorageBucket(); // Firebase Storage 버킷 이름
        Bucket bucket = StorageClient.getInstance().bucket(bucketName);
        Blob blob = bucket.get(file);
        byte[] fileBytes = blob.getContent();

        // 첫 두 바이트를 읽어와서 파일 시그니처를 확인
        int signature = ((int) fileBytes[0] & 0xFF) << 8 | (int) fileBytes[1] & 0xFF;

        if (signature == 0xFFD8 || signature == 0x8950){
            return blob.getContent();
        }else{
            // 오류 처리: 예외를 던집니다.
            throw new IOException("Unknown file type");
        }
    }
}
