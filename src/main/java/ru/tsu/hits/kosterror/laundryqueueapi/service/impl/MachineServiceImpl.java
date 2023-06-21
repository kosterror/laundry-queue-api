package ru.tsu.hits.kosterror.laundryqueueapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.machine.ChangeMachineStatusDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.machine.CreateNewMachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.machine.MachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Machine;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.QueueSlot;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.SlotStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.mapper.MachineMapper;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.MachineRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.QueueSlotRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.service.DormitoryService;
import ru.tsu.hits.kosterror.laundryqueueapi.service.MachineService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MachineServiceImpl implements MachineService {

    @Value("${application.queue-size}")
    private int queueSize;

    private final MachineRepository machineRepository;
    private final MachineMapper machineMapper;
    private final DormitoryService dormitoryService;
    private final QueueSlotRepository queueSlotRepository;

    @Override
    public List<MachineDto> getMachines(UUID dormitoryId) {
        var dormitory = dormitoryService.findDormitory(dormitoryId);
        return machineRepository
                .findAllByLocationOrderByNameAsc(dormitory)
                .stream()
                .map(machineMapper::machineToMachineDto)
                .toList();
    }

    @Transactional
    @Override
    public MachineDto createNewMachine(CreateNewMachineDto createNewMachineDto) {
        var dormitory = dormitoryService.findDormitory(createNewMachineDto.getLocation());
        Machine machine = machineMapper.machineDtoToEntity(createNewMachineDto, dormitory);

        List<QueueSlot> queue = new ArrayList<>();
        for (int queueNumber = 1; queueNumber <= queueSize; queueNumber++) {
            queue.add(
                    QueueSlot
                            .builder()
                            .machine(machine)
                            .number(queueNumber)
                            .status(SlotStatus.FREE)
                            .build()
            );
        }

        machine.setQueueSlots(queue);
        machineRepository.save(machine);
        queueSlotRepository.saveAll(queue);
        return machineMapper.machineToMachineDto(machine);
    }

    @Transactional
    @Override
    public MachineDto changeMachineStatus(ChangeMachineStatusDto changeMachineStatusDto) {
        var machine = findMachine(changeMachineStatusDto.getMachineId());
        machine.setStatus(changeMachineStatusDto.getStatus());
        machine
                .getQueueSlots()
                .forEach(slot -> {
                    slot.setPerson(null);
                    slot.setStatusChanged(LocalDateTime.now());
                    slot.setStatus(SlotStatus.FREE);
                });

        queueSlotRepository.saveAll(machine.getQueueSlots());
        machineRepository.save(machine);
        return machineMapper.machineToMachineDto(machine);
    }

    private Machine findMachine(UUID id) {
        return machineRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Машины с таким id " + id + " найдено не было"));
    }

    @Override
    public void deleteMachine(UUID machineId) {
        machineRepository.deleteById(machineId);
    }

}
