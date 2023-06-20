package ru.tsu.hits.kosterror.laundryqueueapi.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.machine.CreateNewMachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.machine.MachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Dormitory;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Machine;

@Component
@RequiredArgsConstructor
public class MachineMapper {

    private final QueueMapper queueMapper;

    public MachineDto machineToMachineDto(Machine machine) {
        return new MachineDto(
                machine.getId(),
                machine.getStartTime(),
                machine.getType(),
                machine.getStatus(),
                machine.getQueueSlots().stream().map(queueMapper::entityToSlot).sorted().toList(),
                machine.getLocation().getId()
        );
    }

    public Machine machineDtoToEntity(CreateNewMachineDto machineDto, Dormitory dormitory) {
        return Machine
                .builder()
                .type(machineDto.getType())
                .status(machineDto.getMachineStatus())
                .location(dormitory)
                .build();
    }

}
