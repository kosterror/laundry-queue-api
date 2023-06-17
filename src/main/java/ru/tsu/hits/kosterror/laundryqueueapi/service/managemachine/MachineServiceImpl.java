package ru.tsu.hits.kosterror.laundryqueueapi.service.managemachine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.ChangeMachineStatusDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.CreateNewMachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.MachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Machine;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.mapper.MachineMapper;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.MachineRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;
    private final MachineMapper machineMapper;
    private final DormitoryServiceImpl dormitoryServiceImpl;

    @Override
    public List<MachineDto> getMachines(UUID dormitoryId) {

        List<Machine> machines = machineRepository.findAllByLocation(dormitoryServiceImpl.findDormitory(dormitoryId))
                .orElseThrow(() ->
                        new NotFoundException("Машины в общежитии c id " + dormitoryId + " не были найдены"));
        List<MachineDto> machineDtos = new ArrayList<>();
        for (Machine machine : machines) {
            machineDtos.add(machineMapper.machineToMachineDto(machine));
        }
        return machineDtos;
    }

    @Override
    public MachineDto createNewMachine(CreateNewMachineDto createNewMachineDto) {

        Machine machine = machineMapper.machineDtoToEntity(
                createNewMachineDto,
                dormitoryServiceImpl.findDormitory(
                        createNewMachineDto.getLocation()
                ));
        machineRepository.save(machine);
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
    public void deleteMachine(UUID machineId){
        machineRepository.deleteById(machineId);
    }

}
