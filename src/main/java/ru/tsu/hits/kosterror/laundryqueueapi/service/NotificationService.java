package ru.tsu.hits.kosterror.laundryqueueapi.service;

import java.util.UUID;

public interface NotificationService {

    void sendNotification(UUID personId, String title, String body);

}
