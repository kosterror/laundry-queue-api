package ru.tsu.hits.kosterror.laundryqueueapi.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendLaundryFinished(Person person) {
        //TODO реализовать
    }

    @Override
    public void sendYouAreNext(Person person) {
        //TODO реализовать
    }

    @Override
    public void sendYouCanBeNext(List<Person> persons) {
        //TODO реализовать
    }

}
