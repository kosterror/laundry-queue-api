package ru.tsu.hits.kosterror.laundryqueueapi.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NotificationConstants {
    public static final String LAUNDRY_FINISHED_TITLE = "Машина закончила работу!";
    public static final String LAUNDRY_FINISHED_BODY = "Поскорее поторапливайся забирать вещи. " +
            "Машина закончила стираться";
    public static final String YOU_CAN_BE_NEXT_TITLE = "Ты можешь получить услуги раньше!";
    public static final String YOU_CAN_BE_NEXT_BODY = "Один из слотов очереди перед тобой освободился. " +
            "Ты можешь перезаписаться, чтобы получить услуги раньше!";
    public static final String YOU_NEXT_TITLE = "Машина освободилась!";
    public static final String YOU_NEXT_BODY = "Машина освободилась, поскорее поторапливайся, у тебя есть 5 минут," +
            " чтобы воспользоваться услугой, иначе система исключит тебя из очереди!";
}
