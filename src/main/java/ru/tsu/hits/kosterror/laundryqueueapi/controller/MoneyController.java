package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Баланс")
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

    @Operation(
            summary = "Вывести деньги",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/decrease")
    public PersonDto decreaseBalance(@RequestParam BigDecimal delta,
                                     Authentication auth) {
        return moneyService
                .decreaseBalance(
                        ((PersonData) auth.getPrincipal()).getId(),
                        delta
                );
    }

}
