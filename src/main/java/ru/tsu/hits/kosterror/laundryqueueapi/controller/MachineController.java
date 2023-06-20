package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.machine.ChangeMachineStatusDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.machine.CreateNewMachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.machine.MachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.service.managemachine.MachineService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/machines")
@RequiredArgsConstructor
@Tag(name = "Машины")
public class MachineController {

    private final MachineService machineService;

    @Operation(
            summary = "Получить данные о машинах в общежитии."
    )
    @GetMapping("/{dormitoryId}")
    public List<MachineDto> getMachines(@PathVariable UUID dormitoryId) {
        return machineService.getMachines(dormitoryId);
    }

    @Operation(
            summary = "Создать новую машину.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public MachineDto createNewMachine(
            @Valid @RequestBody CreateNewMachineDto createNewMachineDto) {
        return machineService.createNewMachine(createNewMachineDto);
    }

    @Operation(
            summary = "Изменить статус машины.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping
    public MachineDto changeMachineStatus(
            @Valid @RequestBody ChangeMachineStatusDto changeMachineStatusDto) {
        return machineService.changeMachineStatus(changeMachineStatusDto);
    }

    @Operation(
            summary = "Удалить машину.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{machineId}")
    public void deleteMachine(@PathVariable UUID machineId) {
        machineService.deleteMachine(machineId);
    }


}
