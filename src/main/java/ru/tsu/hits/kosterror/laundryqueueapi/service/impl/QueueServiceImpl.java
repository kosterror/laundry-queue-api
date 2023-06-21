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
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.Role;
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

    @Transactional(readOnly = true)
    @Override
    public List<QueueSlotDto> getQueueByMachine(UUID machineId) {
        var machine = machineRepository
                .findById(machineId)
                .orElseThrow(() -> new NotFoundException(String.format("Машина с id %s не найден", machineId)));

        log.info("Размер очереди {}", machine.getQueueSlots().size());
        var slots = machine.getQueueSlots().stream().distinct().toList();

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

        var director = machine
                .getLocation()
                .getPerson()
                .stream()
                .filter(el -> el.getRole() == Role.ROLE_ADMIN)
                .findFirst()
                .orElse(null);

        person.setMoney(money.subtract(price));
        machine.setStatus(MachineStatus.WORKING);
        machine.setStartTime(LocalDateTime.now());

        if (director == null) {
            log.warn("У общежития {} нет директора", machine.getLocation().getId());
        } else {
            director.setMoney(director.getMoney().add(price));
            personRepository.save(director);
        }

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
            throw new BadRequestException("Ты уже есть в какой-то очереди!");
        }

        if (person.getMoney().compareTo(price) < 0) {
            throw new BadRequestException("У тебя недостаточно средств!");
        }

        var slot = queueSlotRepository
                .findById(slotId)
                .orElseThrow(() -> new NotFoundException("Я не смог найти такой слот!"));

        if (slot.getPerson() != null) {
            throw new ConflictException("Слот уже занят!");
        }

        if (slot.getStatus() == SlotStatus.BLOCKED) {
            throw new BadRequestException("Слот заблокирован для записи!");
        }

        if (slot.getMachine().getStatus() == MachineStatus.UNAVAILABLE) {
            throw new BadRequestException("Машина не работает!");
        }

        log.info("Размер очереди: {}", slot.getMachine().getQueueSlots().size());
        var slots = slot.getMachine().getQueueSlots().stream().distinct().sorted().toList();

        int indexLastBusySlot = -1;
        for (int i = 0; i < queueSize; i++) {
            if (slots.get(i).getPerson() != null) {
                indexLastBusySlot = i;
            }
        }

        if (indexLastBusySlot != -1) {
            if (slot.getNumber() - 1 > indexLastBusySlot + 1) {
                throw new BadRequestException("При записи в очередь нельзя образовывать новые окна!");
            }
        } else {
            if (slot.getNumber() != 1) {
                throw new BadRequestException("В пустой очереди можно записаться только в самое начало!");
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
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        var queueSlot = person.getQueueSlot();

        if (queueSlot == null) {
            throw new ConflictException("Ты не записан в очередь!");
        }

        queueSlot.setStatus(SlotStatus.FREE);
        queueSlot.setStatusChanged(LocalDateTime.now());
        queueSlot.setPerson(null);
        queueSlot = queueSlotRepository.save(queueSlot);


        var machine = machineRepository
                .findById(queueSlot.getMachine().getId())
                .orElseThrow(() -> new NotFoundException("Я не смог найти такую машину!"));
        log.info("Размер очереди {}", machine.getQueueSlots().size());
        var queue = machine.getQueueSlots().stream().distinct().sorted().toList();

        for (int i = queueSlot.getNumber(); i < queue.size(); i++) {
            var nextPerson = queue.get(i).getPerson();
            log.info("index: {}, personId : {}", i, nextPerson == null ? null : nextPerson.getId());

            if (nextPerson != null) {
                notificationService.sendNotification(
                        nextPerson.getId(),
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
            throw new BadRequestException("Закрой дверь!", e);
        } catch (RestClientException e) {
            throw new InternalServerException("Ошибка во время запуска машины", e);
        }

    }

}
