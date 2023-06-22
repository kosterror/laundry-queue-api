package ru.tsu.hits.kosterror.laundryqueueapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.queue.QueueSlotDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Machine;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.SlotStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.BadRequestException;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.ConflictException;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.InternalServerException;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.mapper.QueueMapper;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.MachineRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.PersonRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.QueueSlotRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.service.NotificationService;
import ru.tsu.hits.kosterror.laundryqueueapi.service.QueueService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    @Value("${application.queue-size}")
    private int queueSize;

    @Value("${application.price}")
    private BigDecimal price;

    private final MachineRepository machineRepository;
    private final PersonRepository personRepository;
    private final QueueSlotRepository queueSlotRepository;
    private final QueueMapper queueMapper;
    private final RestTemplate restTemplate;
    private final NotificationService notificationService;

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

        return slots.stream().sorted()
                .map(queueMapper::entityToSlot)
                .toList();
    }

    @Transactional
    @Override
    public void startLaundry(UUID personId) {
        var person = personRepository
                .findById(personId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        var slot = person.getQueueSlot();

        if (slot == null || slot.getNumber() != 1) {
            throw new BadRequestException("Вы не находитесь в начале очереди");
        }

        var machine = slot.getMachine();

        if (machine.getStatus() != MachineStatus.READY_TO_WORK) {
            throw new BadRequestException("Машина не готова к запуску");
        }

        BigDecimal money = person.getMoney();

        if (money.compareTo(price) < 0) {
            throw new BadRequestException("У вас недостаточно средств");
        }

        person.setMoney(money.subtract(price));
        machine.setStatus(MachineStatus.WORKING);
        machine.setStartTime(LocalDateTime.now());

        launchMachine(machine);

        personRepository.save(person);
        machineRepository.save(machine);
    }

    @Transactional
    @Override
    public List<QueueSlotDto> signUpForQueue(UUID personId, UUID slotId) {
        var person = personRepository
                .findById(personId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (person.getQueueSlot() != null) {
            throw new BadRequestException("Вы уже находитесь в очереди");
        }

        if (person.getMoney().compareTo(price) < 0) {
            throw new BadRequestException("У вас недостаточно средств");
        }

        var slot = queueSlotRepository
                .findById(slotId)
                .orElseThrow(() -> new NotFoundException("Такой слот не найден"));

        if (slot.getPerson() != null) {
            throw new ConflictException("Слот занят");
        }

        var slots = slot.getMachine().getQueueSlots().stream().sorted().toList();

        int indexLastBusySlot = -1;
        for (int i = 0; i < queueSize; i++) {
            if (slots.get(i).getPerson() != null) {
                indexLastBusySlot = i;
            }
        }

        if (indexLastBusySlot != -1) {
            if (slot.getNumber() - 1 > indexLastBusySlot + 1) {
                throw new BadRequestException("В очередь можно записываться сразу после последнего человека, или " +
                        "в окна перед ним. Нельзя при записи создавать новые окна в очереди");
            }
        } else {
            if (slot.getNumber() != 1) {
                throw new BadRequestException("Если очередь пустая, то можно записаться только в начало");
            }
        }

        slot.setPerson(person);
        slot.setStatusChanged(LocalDateTime.now());
        slot.setStatus(SlotStatus.BUSY);
        slot = queueSlotRepository.save(slot);

        return slot
                .getMachine()
                .getQueueSlots()
                .stream()
                .map(queueMapper::entityToSlot)
                .sorted()
                .toList();
    }

    @Transactional
    @Override
    public List<QueueSlotDto> existFromQueue(UUID personId) {
        var person = personRepository
                .findById(personId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + personId + " не найден"));

        var queueSlot = person.getQueueSlot();

        if (queueSlot == null) {
            throw new ConflictException("Пользователь не находится в очереди");
        }

        queueSlot.setStatus(SlotStatus.FREE);
        queueSlot.setStatusChanged(LocalDateTime.now());
        var removedPerson = queueSlot.getPerson();
        queueSlot.setPerson(null);
        queueSlot = queueSlotRepository.save(queueSlot);

        var queue = queueSlot.getMachine().getQueueSlots().stream().sorted().toList();

        for (int i = queueSlot.getNumber(); i < queue.size(); i++) {
            var nextPerson = queue.get(i).getPerson();

            if (nextPerson != null && !nextPerson.equals(removedPerson)) {
                notificationService.sendInfoNotification(
                        person,
                        "Очередь перед вами освободилась!",
                        "Очередь в слоте " + queueSlot.getNumber() + " освободилась, скорее перезапишитесь!"
                );
            }
        }

        return queue.stream().map(queueMapper::entityToSlot).sorted().toList();
    }

    private void launchMachine(Machine machine) {
        if (machine.getIp() == null) {
            throw new InternalServerException(String.format("Нарушена целостность данных, ip у машины" +
                    " %s равен null", machine.getId()));
        }

        try {
            Object response = restTemplate.postForObject(machine.getIp() + "/start", null, Object.class);
            log.info("Ответ на запуск машины: {}", response);
        } catch (HttpClientErrorException.Conflict e) {
            log.error("Ошибка во время запуска стиральной машины", e);
            throw new BadRequestException("Проверьте закрыта ли дверь и попробуйте еще раз");
        } catch (RestClientException e) {
            throw new InternalServerException("Ошибка во время запуска машины", e);
        }

    }

}
