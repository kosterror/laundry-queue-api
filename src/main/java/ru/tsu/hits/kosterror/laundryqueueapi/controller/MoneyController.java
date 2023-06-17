package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.PersonDto;
import ru.tsu.hits.kosterror.laundryqueueapi.security.PersonData;
import ru.tsu.hits.kosterror.laundryqueueapi.service.money.MoneyService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/money")
public class MoneyController {

    private final MoneyService moneyService;

    @Operation(
            summary = "Пополнить баланс",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/increase")
    public PersonDto increaseBalance(@RequestParam BigDecimal delta,
                                     Authentication auth) {
        return moneyService
                .increaseBalance(
                        ((PersonData) auth.getPrincipal()).getId(),
                        delta
                );
    }

}
