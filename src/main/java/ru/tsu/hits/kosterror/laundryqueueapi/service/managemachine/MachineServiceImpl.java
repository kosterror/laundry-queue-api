package ru.tsu.hits.kosterror.laundryqueueapi.service.managemachine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.ChangeMachineStatusDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.CreateNewMachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.MachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Machine;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.QueueSlot;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.mapper.MachineMapper;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.MachineRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.QueueSlotRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.service.dormitory.DormitoryService;

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
        List<Machine> machines = dormitoryService.findDormitory(dormitoryId).getMachines();

        return machines
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
                            .number(queueNumber + 1)
                            .build()
            );
        }

        machine.setQueueSlots(queue);
        machineRepository.save(machine);
        queueSlotRepository.saveAll(queue);
        return machineMapper.machineToMachineDto(machine);
    }

    @Override
    public MachineDto changeMachineStatus(ChangeMachineStatusDto changeMachineStatusDto) {
        Machine machine = findMachine(changeMachineStatusDto.getMachineId());
        machine.setStatus(changeMachineStatusDto.getStatus());
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
