package ru.tsu.hits.kosterror.laundryqueueapi.service.money;

import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.PersonDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface MoneyService {
    @Transactional
    PersonDto increaseBalance(UUID personId, BigDecimal delta);

    @Transactional
    PersonDto decreaseBalance(UUID personId, BigDecimal delta);
}
