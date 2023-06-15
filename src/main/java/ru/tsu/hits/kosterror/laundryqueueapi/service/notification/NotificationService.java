package ru.tsu.hits.kosterror.laundryqueueapi.service.notification;

import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;

import java.util.List;

public interface NotificationService {

    void sendLaundryFinished(Person person);

    void sendYouAreNext(Person person);

    void sendYouCanBeNext(List<Person> persons);

}
