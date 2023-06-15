package ru.tsu.hits.kosterror.laundryqueueapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan("ru.tsu.hits.kosterror.laundryqueueapi")
public class LaundryQueueApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaundryQueueApiApplication.class, args);
    }

}
