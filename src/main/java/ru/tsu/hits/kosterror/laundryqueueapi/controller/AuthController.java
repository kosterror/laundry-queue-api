package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.ApiResponse;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.PersonCredentials;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.StringObject;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.TokenDto;
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
    public ApiResponse<TokenDto> login(@RequestBody @Valid PersonCredentials credentials) {
        return authService.login(credentials);
    }

    @Operation(
            summary = "Выйти",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/logout")
    public ApiResponse<StringObject> logout(@RequestBody StringObject refreshToken,
                                            Authentication authentication) {
        return authService.logout(
                ((PersonData) authentication.getPrincipal()).getId(),
                refreshToken
        );
    }

    @Operation(
            summary = "Защищенный эндпоинт",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/secured")
    public String securedMethod(Authentication authentication) {
        String email = ((PersonData) authentication.getPrincipal()).getEmail();
        return "Привет, пользователь с email: " + email;
    }

    @Operation(
            summary = "Незащищенный эндпоинт"
    )
    @GetMapping("/unsecured")
    public String unsecuredMethod() {
        return "Это незащищенный эндпоинт";
    }


}
