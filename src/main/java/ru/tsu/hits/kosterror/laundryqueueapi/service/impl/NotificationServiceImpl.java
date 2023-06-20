package ru.tsu.hits.kosterror.laundryqueueapi.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.NotificationType;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.InternalServerException;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.PersonRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.service.NotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final String TYPE_KEY = "TYPE";
    private final FirebaseMessaging firebaseMessaging;
    private final PersonRepository personRepository;

    @Override
    public void sendInfoNotification(Person person, String title, String body) {
        log.info(
                "Отправка пуш-уведомления пользователю с id {}. Заголовок: '{}' Тело: '{}'",
                person.getId(),
                title,
                body
        );
        var notification = buildNotification(title, body);
        var messages = buildMessages(
                person,
                notification,
                Map.of(TYPE_KEY, NotificationType.INFO.toString())
        );
        sendMessages(messages);
    }

    @Override
    public void sendNotification(UUID personId, NotificationType type) {
        var person = personRepository
                .findById(personId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        switch (type) {
            case INFO -> sendInfoNotification(person);
            case YOU_ARE_NEXT -> sendYouAreNext(person);
            case YOU_CAN_BE_NEXT -> sendYouCanBeNext(List.of(person));
            case LAUNDRY_FINISHED -> sendLaundryFinished(person);
        }
    }

    @Override
    public void sendLaundryFinished(Person person) {
        log.info("Отправка уведомления об окончании стирки для пользователя с id {}", person.getId());
        var notification = buildNotification(
                "Стирка закончилась",
                "Стирка закончилась, вам необходимо забрать ваши вещи"
        );

        var messages = buildMessages(
                person,
                notification,
                Map.of(TYPE_KEY, NotificationType.LAUNDRY_FINISHED.toString())
        );

        sendMessages(messages);
        log.info("Отправка уведомления об окончании стирки для пользователя с id {} успешно завершена", person.getId());
    }

    @Override
    public void sendYouAreNext(Person person) {
        log.info("Отправка уведомления о том, что пользователь с id {} следующий в очереди", person.getId());
        var notification = buildNotification(
                "Скорее загружайте вещи!",
                "У вас есть 5 минут, чтобы загрузить вещи в машину! Если вы не уложитесь в эти 5 минут, " +
                        "то потеряете место в очереди"
        );
        var messages = buildMessages(
                person,
                notification,
                Map.of(TYPE_KEY, NotificationType.YOU_ARE_NEXT.toString())
        );
        sendMessages(messages);
        log.info("Отправка уведомления о том, что пользователь с id {} следующий в очереди успешно завершена", person.getId());
    }

    @Override
    public void sendYouCanBeNext(List<Person> persons) {
        for (var person : persons) {
            log.info(
                    "Отправка уведомления пользователю с id {} о том, что он может быть следующим",
                    person.getId()
            );
            var notification = buildNotification(
                    "У вас есть шанс постираться прямо сейчас",
                    "Нажмите на это уведомление и подтвердите, что вы готовы в течение 5 минут пойти стираться," +
                            "чтобы быть следующим!"
            );
            var messages = buildMessages(
                    person,
                    notification,
                    Map.of(TYPE_KEY, NotificationType.YOU_CAN_BE_NEXT.toString())
            );
            sendMessages(messages);
            log.info("Отправка уведомления пользователю с id {} о том, что он может " +
                    "быть следующим успешно завершена", person.getId());
        }
    }

    public void sendInfoNotification(Person person) {
        log.info("Отправка обычного уведомления пользователю с id {}", person.getId());
        var notification = buildNotification(
                "Уведомление с обычной информацией",
                "Тело уведомления с обычной информацией"
        );
        var messages = buildMessages(
                person,
                notification,
                Map.of(TYPE_KEY, NotificationType.INFO.toString())
        );
        sendMessages(messages);
        log.info("Уведомление с обычной информацией успешно отправилось пользователю с id {}", person.getId());
    }

    private Notification buildNotification(String title, String body) {
        return Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }

    private List<Message> buildMessages(Person person, Notification notification, Map<String, String> data) {
        List<Message> messages = new ArrayList<>(person.getDeviceTokens().size());
        for (var deviceToken : person.getDeviceTokens()) {
            if (deviceToken.getToken() != null) {
                messages.add(
                        Message
                                .builder()
                                .setNotification(notification)
                                .setToken(deviceToken.getToken())
                                .putAllData(data)
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
        try {
            messages.forEach(this::sendMessage);
        } catch (InternalServerException e) {
            log.error("Не удалось отправить уведомление пользователю", e);
        }
    }

    private void sendMessage(Message message) {
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Не удалось отправить уведомление пользователю", e);
            throw new InternalServerException("Не удалось отправить уведомление", e);
        }
    }

}
