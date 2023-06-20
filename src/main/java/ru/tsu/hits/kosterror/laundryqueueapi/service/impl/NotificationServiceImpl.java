package ru.tsu.hits.kosterror.laundryqueueapi.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.InternalServerException;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.PersonRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.service.NotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final PersonRepository personRepository;

    @Override
    public void sendNotification(Person person, String title, String body) {
        log.info("Отправка уведомления пользователю с id {}. title {} body {}", person.getId(), title, body);
        var notification = buildNotification(title, body);
        var messages = buildMessages(person, notification);
        sendMessages(messages);
    }

    @Override
    public void sendNotification(UUID personId, String title, String body) {
        var person = personRepository
                .findById(personId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        sendNotification(person, title, body);
    }

    private Notification buildNotification(String title, String body) {
        return Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }

    private List<Message> buildMessages(Person person, Notification notification) {
        List<Message> messages = new ArrayList<>(person.getDeviceTokens().size());
        for (var deviceToken : person.getDeviceTokens()) {
            if (deviceToken.getToken() != null) {
                messages.add(
                        Message
                                .builder()
                                .setNotification(notification)
                                .setToken(deviceToken.getToken())
                                .build()
                );
            } else {
                log.warn(
                        "Токен девайса c id {} у пользователя с id {} равен null",
                        deviceToken.getId(),
                        person.getId()
                );
            }
        }
        return messages;
    }

    private void sendMessages(List<Message> messages) {
        for (var message : messages) {
            try {
                sendMessage(message);
            } catch (InternalServerException e) {
                log.error("Не удалось отправить уведомление пользователю", e);
            }
        }
    }

    private void sendMessage(Message message) {
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new InternalServerException("Не удалось отправить уведомление", e);
        }
    }

}
