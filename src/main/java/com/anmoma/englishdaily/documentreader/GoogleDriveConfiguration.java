package com.anmoma.englishdaily.documentreader;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleDriveConfiguration {
    @Bean
    public Drive driveService(@Value("${google.drive.credentials.path}") String credentialsPath) {
        try {
            InputStream serviceAccountStream = GoogleDriveFolderReader.class.getClassLoader()
                                                                            .getResourceAsStream(credentialsPath);
            if (serviceAccountStream == null) {
                throw new RuntimeException("Service account file not found in classpath: " + credentialsPath);
            }
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream)
                                                             .createScoped(Collections.singleton("https://www.googleapis.com/auth/drive.readonly"));
            return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials)).setApplicationName("AutonomousDriveAccess")
                                                            .build();
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Failed to create Google Drive service", e);
        }
    }
}
