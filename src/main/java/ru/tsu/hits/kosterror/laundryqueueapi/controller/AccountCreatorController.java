package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.ApiResponse;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.StringObject;
import ru.tsu.hits.kosterror.laundryqueueapi.service.manageaccount.ManageAccountService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Создание аккаунтов")
public class AccountCreatorController {

    private final ManageAccountService manageAccountService;

    @Operation(
            summary = "Создать аккаунт студента",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/creator/student/{email}")
    public ApiResponse<StringObject> createStudent(@PathVariable String email) {
        return manageAccountService.createStudent(email);
    }

}
