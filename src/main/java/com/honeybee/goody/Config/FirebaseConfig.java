package com.honeybee.goody.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class FirebaseConfig {

    @Value("${firestore.serviceAccountKeyPath}")
    private String serviceAccountKeyPath;
    @Bean
    public Firestore firestore() throws IOException {
        InputStream serviceAccount = getClass().getResourceAsStream("/serviceAccountKey.json");
//            FileInputStream serviceAccount =
//                new FileInputStream(serviceAccountKeyPath);
        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://project-test-383014-default-rtdb.firebaseio.com")
            .setStorageBucket("goody-4b16e.appspot.com")
            .build();

        FirebaseApp.initializeApp(options);
        return FirestoreClient.getFirestore();

    }

    @Bean
    public Storage storage() throws IOException {
        InputStream serviceAccount = getClass().getResourceAsStream("/serviceAccountKey.json");
        StorageOptions storageOptions = StorageOptions.newBuilder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

        String projectId = ((GoogleCredentials) storageOptions.getCredentials()).getQuotaProjectId();

        return storageOptions.getService();
    }

}
