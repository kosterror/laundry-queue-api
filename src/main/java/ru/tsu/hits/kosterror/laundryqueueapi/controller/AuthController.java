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
import ru.tsu.hits.kosterror.laundryqueueapi.dto.api.StringObject;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.AuthDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.PersonCredentials;
import ru.tsu.hits.kosterror.laundryqueueapi.security.PersonData;
import ru.tsu.hits.kosterror.laundryqueueapi.service.auth.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Логин, логаут")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Логин"
    )
    @PostMapping("/login")
    public AuthDto login(@RequestBody @Valid PersonCredentials credentials) {
        return authService.login(credentials);
    }

    @Operation(
            summary = "Обновить пару токенов"
    )
    @PostMapping("/refresh")
    public AuthDto refreshTokens(@RequestBody @Valid StringObject refreshToken) {
        return authService.refreshTokens(refreshToken);
    }

    @Operation(
            summary = "Выйти",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/logout")
    public void logout(@RequestBody @Valid StringObject refreshToken,
                       Authentication authentication) {
        authService.logout(
                ((PersonData) authentication.getPrincipal()).getId(),
                refreshToken
        );
    }

}
