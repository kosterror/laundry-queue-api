package ru.tsu.hits.kosterror.laundryqueueapi.service.notification;

import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.NotificationType;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    void sendNotification(UUID personId, NotificationType type);

    void sendLaundryFinished(Person person);

    void sendYouAreNext(Person person);

    void sendYouCanBeNext(List<Person> persons);

}
