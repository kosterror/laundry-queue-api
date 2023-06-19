package ru.tsu.hits.kosterror.laundryqueueapi.service.queue;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.QueueSlotDto;

import java.util.List;
import java.util.UUID;

public interface QueueService {
    List<QueueSlotDto> getQueueByMachine(UUID machineId);
}
