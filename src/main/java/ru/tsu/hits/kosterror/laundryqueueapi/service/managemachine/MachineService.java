package ru.tsu.hits.kosterror.laundryqueueapi.service.managemachine;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.ChangeMachineStatusDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.CreateNewMachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.MachineDto;

import java.util.List;
import java.util.UUID;

public interface MachineService {

    List<MachineDto> getMachines(UUID dormitoryId);

    MachineDto createNewMachine(CreateNewMachineDto createNewMachineDto);

    MachineDto changeMachineStatus(ChangeMachineStatusDto changeMachineStatusDto);

    void deleteMachine(UUID machineId);
}
