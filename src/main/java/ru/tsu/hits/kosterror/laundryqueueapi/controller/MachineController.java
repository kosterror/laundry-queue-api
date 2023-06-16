package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.CreateNewMachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.MachineDto;
import ru.tsu.hits.kosterror.laundryqueueapi.security.PersonData;
import ru.tsu.hits.kosterror.laundryqueueapi.service.MachineService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MachineController {

    private final MachineService machineService;

    @Operation(
            summary = "Получить данные о машиных в общежитии.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/machine/{dormitoryId}")
    public List<MachineDto> getMachines(Authentication authentication, @PathVariable UUID dormitoryId) {

       return machineService.getMachines(dormitoryId);
    }

    @Operation(
            summary = "Создать новую машину.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/machine")
    public MachineDto createNewMachine(Authentication authentication, @RequestBody CreateNewMachineDto createNewMachineDto) {

       return machineService.createNewMachine(createNewMachineDto);
    }
//    @Operation(
//            summary = "Изменить статус машины.",
//            security = @SecurityRequirement(name = "bearerAuth")
//    )
//    @PatchMapping("/machine/{dormitoryId}")
//    public MachineDto createNewMachine(Authentication authentication) {
//
//      // return machineService.createNewMachine(createNewMachineDto);
//    }


}
