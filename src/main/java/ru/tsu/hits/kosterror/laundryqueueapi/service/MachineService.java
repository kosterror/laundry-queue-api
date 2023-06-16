package ru.tsu.hits.kosterror.laundryqueueapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.CreateNewMachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.MachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Dormitory;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Machine;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.mapper.MachineMapper;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.DormitoryRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.MachineRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MachineService {

    private final MachineRepository machineRepository;
    private final MachineMapper machineMapper;
    private final DormitoryService dormitoryService;

    public List<MachineDto> getMachines(UUID dormitoryId) {

        List<Machine> machines = machineRepository.findAllByLocation(dormitoryService.findDormitory(dormitoryId))
                .orElseThrow(() -> new NotFoundException("Машины в этом общежитии не были найдены"));
        List<MachineDto> machineDtos = new ArrayList<>();
        for (Machine machine : machines) {
            machineDtos.add(machineMapper.machineToMachineDto(machine));
        }
        return machineDtos;
    }

    public MachineDto createNewMachine(CreateNewMachineDto createNewMachineDto) {

        Machine machine = machineMapper.machineDtoToEntity(
                createNewMachineDto,
                dormitoryService.findDormitory(
                        createNewMachineDto.getLocation()
                ));
        machineRepository.save(machine);
        return machineMapper.machineToMachineDto(machine);
    }


}
