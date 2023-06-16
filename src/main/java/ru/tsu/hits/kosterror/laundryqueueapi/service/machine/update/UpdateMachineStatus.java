package ru.tsu.hits.kosterror.laundryqueueapi.service.machine.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Machine;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.QueueSlot;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.InternalServerException;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.MachineRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.QueueSlotRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.service.notification.NotificationService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateMachineStatus {

    @Value("${application.queue-size}")
    private int queueSize;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
    private final MachineRepository machineRepository;
    private final QueueSlotRepository queueSlotRepository;
    private final NotificationService notificationService;


    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 1)
    private void reportCurrentTime() {
        log.info("Текущее время {}", dateFormat.format(new Date()));
    }

    private void updateMachinesStatus() {
        List<Machine> workingMachines = machineRepository.findAllByStatus(MachineStatus.WORKING);

        for (var machine : workingMachines) {
            boolean isWorking = new Random().nextBoolean(); //TODO: заменить на интеграционный запрос
            if (!isWorking) {
                updateMachineStatus(machine);
            }
        }

        machineRepository.saveAll(workingMachines);
    }

    private void updateMachineStatus(Machine machine) {
        List<QueueSlot> queue = queueSlotRepository.findAllByMachineOrderByNumberAsc(machine);

        if (queueSize < 2 || queue.size() != queueSize) {
            throw new InternalServerException("Размер очереди неправильного размера для " +
                    "стиральной машины: " + machine.getId());
        }

        Person currentPerson = queue.get(0).getPerson();
        Person nextPerson = queue.get(1).getPerson();

        notificationService.sendLaundryFinished(currentPerson);
        notificationService.sendYouAreNext(nextPerson);

        machine.setStatus(MachineStatus.READY_TO_WORK);
    }

}
