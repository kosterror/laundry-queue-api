package ru.tsu.hits.kosterror.laundryqueueapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.api.StatusDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Machine;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.QueueSlot;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.SlotStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.MachineRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.QueueSlotRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.tsu.hits.kosterror.laundryqueueapi.util.NotificationConstants.*;
import static ru.tsu.hits.kosterror.laundryqueueapi.util.QueueConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateMachineStatus {

    @Value("${application.queue-size}")
    private int queueSize;

    private final MachineRepository machineRepository;
    private final QueueSlotRepository queueSlotRepository;
    private final NotificationService notificationService;
    private final RestTemplate restTemplate;

    @Transactional
    @Scheduled(timeUnit = TimeUnit.SECONDS, fixedRate = 5)
    public void updateMachinesStatus() {
        List<Machine> workingMachines = machineRepository.findAllByStatus(MachineStatus.WORKING);

        for (var machine : workingMachines) {
            try {
                var response = restTemplate.getForObject(machine.getIp() + "/status", StatusDto.class);
                if (response != null && response.getIsWorking() == 0) {
                    updateMachineStatus(machine);
                    queueSlotRepository.saveAll(machine.getQueueSlots());
                }
            } catch (RestClientException e) {
                log.error("Ошибка во время интеграционного запроса для обновления статуса машины", e);
            }
        }

        machineRepository.saveAll(workingMachines);
    }

    @Transactional
    @Scheduled(timeUnit = TimeUnit.SECONDS, fixedRate = 5)
    public void updateIdleMachines() {
        List<Machine> machines = machineRepository.findAllByStatus(MachineStatus.READY_TO_WORK);

        for (var machine : machines) {
            var queue = machine.getQueueSlots().stream().sorted().toList();
            if (queue.size() != queueSize) {
                log.error("Некорректный размер очереди у машины " + machine.getId());
                machine.setStatus(MachineStatus.UNAVAILABLE);
                continue;
            }

            var firstSlot = queue.get(0);

            if (slotIsNotEmpty(firstSlot) && havePassedMin(firstSlot, PENDING_MIN)) {
                log.info("Человек не пришел в течение {} минут", PENDING_MIN);
                firstSlot.setPerson(null);
                firstSlot.setStatus(SlotStatus.FREE);
                firstSlot.setStatusChanged(LocalDateTime.now());
                notifyPersons(queue);
            } else if (!slotIsNotEmpty(firstSlot)
                    && firstSlot.getStatus() == SlotStatus.FREE
                    && queueContainsPersons(queue)
                    && havePassedMin(firstSlot, PAUSE_MIN)) {
                log.info("Никто не записался в течение {} минут, при том, что есть люди в очереди", PAUSE_MIN);
                firstSlot.setStatus(SlotStatus.BLOCKED);
            } else if (firstSlot.getPerson() == null
                    && firstSlot.getStatus() == SlotStatus.BLOCKED) {
                if (queueContainsPersons(queue)) {
                    if (havePassedMin(firstSlot, FROZEN_MIN)) {
                        log.info("Первый слот был заблокирован в течение {} минут, теперь сдвигаем очередь",
                                FROZEN_MIN - PAUSE_MIN);
                        moveQueue(queue);
                    }
                } else {
                    log.info("Первый слот был заблокирован, но теперь очередь пустая, разблокируем слот");
                    firstSlot.setStatus(SlotStatus.FREE);
                    firstSlot.setStatusChanged(LocalDateTime.now());
                }
            }
            queueSlotRepository.saveAll(queue);
        }

        machineRepository.saveAll(machines);
    }

    private void moveQueue(List<QueueSlot> queue) {
        for (int i = 1; i < queue.size(); i++) {
            var cSlot = queue.get(i);
            var pSlot = queue.get(i - 1);
            pSlot.setPerson(cSlot.getPerson());
            pSlot.setStatusChanged(LocalDateTime.now());
            if (slotIsNotEmpty(pSlot)) {
                pSlot.setStatus(SlotStatus.BUSY);
            } else {
                pSlot.setStatus(SlotStatus.FREE);
            }
        }
    }

    private boolean slotIsNotEmpty(QueueSlot firstSlot) {
        return firstSlot.getPerson() != null;
    }

    private void notifyPersons(List<QueueSlot> queue) {
        for (int i = 1; i < queue.size(); i++) {
            var currentPerson = queue.get(i).getPerson();
            if (currentPerson != null) {
                try {
                    notificationService.sendNotification(currentPerson.getId(), YOU_CAN_BE_NEXT_TITLE, YOU_CAN_BE_NEXT_BODY);
                } catch (Exception e) {
                    log.error("Ошибка во время отправка уведомления о том, что" +
                            " слот перед человеком освободился", e);
                }
            }
        }
    }

    private boolean queueContainsPersons(List<QueueSlot> queue) {
        for (var slot : queue) {
            if (slotIsNotEmpty(slot)) {
                return true;
            }
        }

        return false;
    }

    private boolean havePassedMin(QueueSlot queueSlot, long minutes) {
        var now = LocalDateTime.now();
        var old = queueSlot.getStatusChanged();
        long diff = old.until(now, ChronoUnit.MINUTES);

        return diff >= minutes;
    }

    private void updateMachineStatus(Machine machine) {
        List<QueueSlot> queue = machine.getQueueSlots().stream().sorted().toList();

        if (queue.size() != queueSize) {
            log.error("Размер очереди неправильного размера у машины {} ", machine.getId());
            return;
        }

        Person currentPerson = queue.get(0).getPerson();
        Person nextPerson = queue.get(1).getPerson();

        if (currentPerson == null) {
            log.warn("Машина закончила работу, но первый слот в очереди пустой");
        } else {
            try {
                notificationService.sendNotification(currentPerson.getId(), LAUNDRY_FINISHED_TITLE, LAUNDRY_FINISHED_BODY);
            } catch (Exception e) {
                log.error(
                        "Ошибка во время отправки уведомления об окончании работы машины пользователю {}",
                        currentPerson.getId(),
                        e
                );
            }
        }

        if (nextPerson != null) {
            try {
                notificationService.sendNotification(nextPerson.getId(), YOU_NEXT_TITLE, YOU_NEXT_BODY);
            } catch (Exception e) {
                log.error("Ошибка во время отправки уведомления о том, что пользователь {} следующий в очереди",
                        nextPerson.getId(), e);
            }
        }

        moveQueue(queue);
        machine.setStatus(MachineStatus.READY_TO_WORK);
    }

}
