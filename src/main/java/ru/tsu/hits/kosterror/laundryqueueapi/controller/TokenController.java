package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.api.DeviceTokenDto;
import ru.tsu.hits.kosterror.laundryqueueapi.security.PersonData;
import ru.tsu.hits.kosterror.laundryqueueapi.service.TokenService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Токены девайсов")
public class TokenController {

    private final TokenService tokenService;

    @Operation(
            summary = "Добавить новый токен девайса",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/token")
    public void createNewToken(@Valid @RequestBody DeviceTokenDto deviceTokenDto,
                               Authentication auth) {
        tokenService.saveDeviceToken(((PersonData) auth.getPrincipal()).getId(), deviceTokenDto);
    }

}
