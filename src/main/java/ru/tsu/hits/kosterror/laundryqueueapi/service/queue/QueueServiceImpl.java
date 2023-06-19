package ru.tsu.hits.kosterror.laundryqueueapi.service.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.QueueSlotDto;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.InternalServerException;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.mapper.QueueMapper;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.MachineRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    @Value("${application.queue-size}")
    private int queueSize;

    private final MachineRepository machineRepository;
    private final QueueMapper queueMapper;

    @Override
    public List<QueueSlotDto> getQueueByMachine(UUID machineId) {
        var machine = machineRepository
                .findById(machineId)
                .orElseThrow(() -> new NotFoundException(String.format("Машина с id %s не найден", machineId)));

        var slots = machine.getQueueSlots();

        if (slots.size() != queueSize) {
            throw new InternalServerException(String.format("Нарушена целостность данных, у машины %s очередь " +
                    "некорректного размера %s", machine.getId(), slots.size()));
        }

        return slots.stream().sorted((slot1, slot2) -> {
                    if (slot1.getNumber() < slot2.getNumber()) {
                        return -1;
                    } else if (slot1.getNumber() > slot2.getNumber()) {
                        return 1;
                    }
                    return 0;
                })
                .map(queueMapper::entityToSlot)
                .toList();
    }

}
