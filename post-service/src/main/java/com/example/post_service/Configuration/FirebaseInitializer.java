package com.example.post_service.Configuration;

import com.google.api.client.util.Lists;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class FirebaseInitializer {
    public static void initialize() throws IOException {
        Resource resource = new ClassPathResource("chatappjava-a7ee2-firebase-adminsdk-aqeah-d133e3e853.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream())
                .createScoped(Lists.newArrayList());

        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build();

        Storage storage = storageOptions.getService();
        // Ensure storage is initialized and authorized
    }
}
