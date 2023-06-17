package ru.tsu.hits.kosterror.laundryqueueapi.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.CreateNewMachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.MachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Dormitory;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Machine;

@Component
@RequiredArgsConstructor
public class MachineMapper {

    public MachineDto machineToMachineDto(Machine machine) {
        return new MachineDto(
                machine.getId(),
                machine.getStartTime(),
                machine.getType(),
                machine.getStatus(),
                machine.getQueueSlots(),
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
