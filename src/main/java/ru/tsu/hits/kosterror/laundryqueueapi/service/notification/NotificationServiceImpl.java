package ru.tsu.hits.kosterror.laundryqueueapi.service.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.NotificationType;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.BadRequestException;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.InternalServerException;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.PersonRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final String TYPE_KEY = "TYPE";
    private final FirebaseMessaging firebaseMessaging;
    private final PersonRepository personRepository;

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
        checkDeviceToken(person);
        var notification = buildNotification(
                "Стирка закончилась",
                "Стирка закончилась, вам необходимо забрать ваши вещи"
        );
        var message = buildMessage(
                notification,
                person.getDeviceToken(),
                Map.of(TYPE_KEY, NotificationType.LAUNDRY_FINISHED.toString())
        );
        sendMessage(message);
    }

    @Override
    public void sendYouAreNext(Person person) {
        checkDeviceToken(person);
        var notification = buildNotification(
                "Скорее загружайте вещи!",
                "У вас есть 5 минут, чтобы загрузить вещи в машину! Если вы не уложитесь в эти 5 минут, " +
                        "то потеряете место в очереди"
        );
        var message = buildMessage(
                notification,
                person.getDeviceToken(),
                Map.of(TYPE_KEY, NotificationType.YOU_ARE_NEXT.toString())
        );
        sendMessage(message);
    }

    @Override
    public void sendYouCanBeNext(List<Person> persons) {
        for (var person : persons) {
            if (person.getDeviceToken() != null) {
                var notification = buildNotification(
                        "У вас есть шанс постираться прямо сейчас",
                        "Нажмите на это уведомление и подтвердите, что вы готовы в течение 5 минут пойти стираться," +
                                "чтобы быть следующим!"
                );
                var message = buildMessage(
                        notification,
                        person.getDeviceToken(),
                        Map.of(TYPE_KEY, NotificationType.YOU_CAN_BE_NEXT.toString())
                );
                sendMessage(message);
            }
        }
    }

    public void sendInfoNotification(Person person) {
        checkDeviceToken(person);
        var notification = buildNotification(
                "Уведомление с обычной информацией",
                "Тело уведомления с обычной информацией"
        );
        var message = buildMessage(
                notification,
                person.getDeviceToken(),
                Map.of(TYPE_KEY, NotificationType.INFO.toString())
        );
        sendMessage(message);
    }

    private Notification buildNotification(String title, String body) {
        return Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }

    private Message buildMessage(Notification notification, String token, Map<String, String> data) {
        return Message
                .builder()
                .setNotification(notification)
                .setToken(token)
                .putAllData(data)
                .build();
    }

    private void checkDeviceToken(Person person) {
        if (person.getDeviceToken() == null) {
            throw new BadRequestException("У данного пользователя нет токена девайса");
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
