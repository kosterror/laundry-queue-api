package ru.tsu.hits.kosterror.laundryqueueapi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.InternalServerException;

import java.io.IOException;
import java.util.Random;

@Configuration
public class AppConfig {

    @Value("${application.firebase-configuration-file}")
    private String firebaseConfigFilename;

    @Value("${application.name}")
    private String applicationName;

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigFilename).getInputStream());

            FirebaseOptions firebaseOptions = FirebaseOptions
                    .builder()
                    .setCredentials(googleCredentials)
                    .build();

            FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, applicationName);
            return FirebaseMessaging.getInstance(app);
        } catch (IOException e) {
            throw new InternalServerException("Ошибка при считывании файла для конфигурации FCM", e);
        }
    }

}
