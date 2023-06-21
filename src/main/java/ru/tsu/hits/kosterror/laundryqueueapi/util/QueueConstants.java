package ru.tsu.hits.kosterror.laundryqueueapi.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class QueueConstants {
//    public static final long PENDING_MIN = 5;
//    public static final long PAUSE_MIN = 15;
//    public static final long FROZEN_MIN = PAUSE_MIN + 25;
    public static final long PENDING_MIN = 1;
    public static final long PAUSE_MIN = 1;
    public static final long FROZEN_MIN = PAUSE_MIN + 1;
}
