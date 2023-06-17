package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.PersonDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.StudentDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.UpdateAdminInfo;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.UpdateStudentInfo;
import ru.tsu.hits.kosterror.laundryqueueapi.security.PersonData;
import ru.tsu.hits.kosterror.laundryqueueapi.service.account.AccountService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Tag(name = "Личный кабинет")
public class AccountController {

    private final AccountService accountService;
    private final ObjectMapper objectMapper;

    @Operation(
            summary = "Получить данные о cебе(студент).",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/student")
    public StudentDto getStudentInfo(Authentication authentication) {
        PersonData personData = objectMapper.convertValue(authentication.getPrincipal(), PersonData.class);
        return accountService.getStudentInfo(personData.getId());
    }

    @Operation(
            summary = "Получить данные о cебе(админ/сотрудник).",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/admin")
    public PersonDto getPersonInfo(Authentication authentication) {
        PersonData personData = objectMapper.convertValue(authentication.getPrincipal(), PersonData.class);
        return accountService.getPersonInfo(personData.getId());
    }


    @Operation(
            summary = "Изменить данные о себe(студент).",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/student")
    public StudentDto changeStudentInfo(Authentication authentication, @Valid @RequestBody UpdateStudentInfo updateStudentInfo) {
        PersonData personData = objectMapper.convertValue(authentication.getPrincipal(), PersonData.class);
        return accountService.changeStudentInfo(personData.getId(), updateStudentInfo);
    }

    @Operation(
            summary = "Изменить данные о себe(админ/сотрудник).",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/account/admin")
    public PersonDto changeAdminInfo(Authentication authentication, @Valid @RequestBody UpdateAdminInfo updateAdminInfo) {
        PersonData personData = objectMapper.convertValue(authentication.getPrincipal(), PersonData.class);
        return accountService.changeAdminInfo(personData.getId(), updateAdminInfo);
    }

}
