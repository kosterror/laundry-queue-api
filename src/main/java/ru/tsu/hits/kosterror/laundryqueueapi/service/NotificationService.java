package ru.tsu.hits.kosterror.laundryqueueapi.service;

import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;

import java.util.UUID;

public interface NotificationService {

    void sendNotification(Person person, String title, String body);

    void sendNotification(UUID personId, String title, String body);

}
