package ru.tsu.hits.kosterror.laundryqueueapi.service;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.queue.QueueSlotDto;

import java.util.List;
import java.util.UUID;

public interface QueueService {
    List<QueueSlotDto> getQueueByMachine(UUID machineId);

    void startLaundry(UUID personId);

    List<QueueSlotDto> signUpForQueue(UUID personId, UUID slotId);

    List<QueueSlotDto> existFromQueue(UUID personId);
}
