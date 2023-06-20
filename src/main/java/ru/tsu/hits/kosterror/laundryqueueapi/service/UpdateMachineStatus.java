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
import ru.tsu.hits.kosterror.laundryqueueapi.exception.InternalServerException;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.MachineRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.QueueSlotRepository;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private final RestTemplate restTemplate;

    @Transactional
    @Scheduled(timeUnit = TimeUnit.SECONDS, fixedRate = 15)
    public void updateMachinesStatus() {
        List<Machine> workingMachines = machineRepository.findAllByStatus(MachineStatus.WORKING);

        for (var machine : workingMachines) {
            try {
                var response = restTemplate.getForObject(machine.getIp() + "/status", StatusDto.class);
                if (response != null && response.getIsWorking() == 0) {
                    updateMachineStatus(machine);
                }
            } catch (RestClientException e) {
                log.error("Ошибка во время интеграционного запроса для обновления статуса машины");
            }
        }

        machineRepository.saveAll(workingMachines);
    }

    private void updateMachineStatus(Machine machine) {
        List<QueueSlot> queue = machine.getQueueSlots().stream().sorted().collect(Collectors.toList());

        if (queueSize < 2 || queue.size() != queueSize) {
            throw new InternalServerException("Размер очереди неправильного размера для " +
                    "стиральной машины: " + machine.getId());
        }

        Person currentPerson = queue.get(0).getPerson();
        Person nextPerson = queue.get(1).getPerson();

        if (currentPerson == null) {
            log.warn("Машина закончила работу, но первый слот в очереди пустой");
        } else {
            try {
                notificationService.sendLaundryFinished(currentPerson);
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
                notificationService.sendYouAreNext(nextPerson);
                //TODO: засечь 5 минут
            } catch (Exception e) {
                log.error(
                        "Ошибка во время отправки уведомления о том, что пользователь {} следующий в очереди",
                        nextPerson.getId(),
                        e
                );
            }
        } else {
            //TODO: отправить всем уведомление и засечь 15 минут
        }

        queue.get(0).setPerson(null);
        for (int i = 1; i < queue.size(); i++) {
            queue.get(i - 1).setPerson(queue.get(i).getPerson());
        }

        machine.setStatus(MachineStatus.READY_TO_WORK);
    }

}
