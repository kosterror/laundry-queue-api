package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.ChangeMachineStatusDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.CreateNewMachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.MachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.security.PersonData;
import ru.tsu.hits.kosterror.laundryqueueapi.service.CheckRoleService;
import ru.tsu.hits.kosterror.laundryqueueapi.service.managemachine.MachineService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MachineController {

    private final MachineService machineService;
    private final CheckRoleService checkRoleService;
    private final ObjectMapper objectMapper;

    @Operation(
            summary = "Получить данные о машинах в общежитии.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/machine/{dormitoryId}")
    public List<MachineDto> getMachines(@PathVariable UUID dormitoryId) {
        return machineService.getMachines(dormitoryId);
    }

    @Operation(
            summary = "Создать новую машину.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/machine")
    public MachineDto createNewMachine(
            Authentication authentication,
            @Valid @RequestBody CreateNewMachineDto createNewMachineDto) {
        checkRoleService.checkAdminRole(objectMapper.convertValue(authentication.getPrincipal(), PersonData.class));
        return machineService.createNewMachine(createNewMachineDto);
    }

    @Operation(
            summary = "Изменить статус машины.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/machine")
    public MachineDto changeMachineStatus(
            Authentication authentication,
            @Valid @RequestBody ChangeMachineStatusDto changeMachineStatusDto) {
        checkRoleService.checkAdminRole(objectMapper.convertValue(authentication.getPrincipal(), PersonData.class));
        return machineService.changeMachineStatus(changeMachineStatusDto);
    }

    @Operation(
            summary = "Удалить машину.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/machine/{machineId}")
    public void deleteMachine(Authentication authentication, @PathVariable UUID machineId) {
        checkRoleService.checkAdminRole(objectMapper.convertValue(authentication.getPrincipal(), PersonData.class));
        machineService.deleteMachine(machineId);
    }


}
