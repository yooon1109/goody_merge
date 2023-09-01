package com.honeybee.goody.File;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
@Service
public class FileService {

    @Value("uploads/")
    private String uploadDir;
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
        Bucket bucket = StorageClient.getInstance().bucket(bucketName);
        InputStream content = new ByteArrayInputStream(multipartFile.getBytes());
        Blob blob = bucket.create("Image/"+multipartFile.getOriginalFilename(),content,multipartFile.getContentType());
        return blob.getName();

    }

    // 파일 내용을 바이트 배열로 가져오는 메서드
    public byte[] getFileContent(String file) throws IOException {

        String bucketName = FirebaseApp.getInstance().getOptions().getStorageBucket(); // Firebase Storage 버킷 이름
        Bucket bucket = StorageClient.getInstance().bucket(bucketName);
        Blob blob = bucket.get(file);

        return blob.getContent();
    }
}
