package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.CreateEmployeeDto;
import ru.tsu.hits.kosterror.laundryqueueapi.service.manageaccount.ManageAccountService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/creator")
@RequiredArgsConstructor
@Tag(name = "Создание аккаунтов")
public class AccountCreatorController {

    private final ManageAccountService manageAccountService;

    @Operation(
            summary = "Создать аккаунт студента",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/student/{email}")
    public void createStudent(@PathVariable String email) {
        manageAccountService.createStudent(email);
    }

    @Operation(
            summary = "Создать аккаунт сотрудника",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/employee")
    public void createEmployee(@RequestBody @Valid CreateEmployeeDto dto) {
        manageAccountService.createEmployee(dto);
    }

}
