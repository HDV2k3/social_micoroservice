package com.example.notification_service.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.file}")
    private String firebaseConfigFile;

    @Value("${firebase.storage.bucket}")
    private String firebaseStorageBucket;

    private final ResourceLoader resourceLoader;

    public FirebaseConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        Resource resource = resourceLoader.getResource(firebaseConfigFile);
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setStorageBucket(firebaseStorageBucket)
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
