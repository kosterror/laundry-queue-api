package ru.tsu.hits.kosterror.laundryqueueapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.PersonDto;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.BadRequestException;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.mapper.PersonMapper;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.PersonRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.service.MoneyService;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoneyServiceImpl implements MoneyService {

    private final PersonMapper personMapper;
    private final PersonRepository personRepository;

    @Override
    @Transactional
    public PersonDto increaseBalance(UUID personId, BigDecimal delta) {
        if (delta.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Некорректная сумма для пополнения: " + delta);
        }

        var person = personRepository
                .findById(personId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        var newBalance = person.getMoney().add(delta);
        person.setMoney(newBalance);
        person = personRepository.save(person);

        return personMapper.entityToAdminDto(person);
    }

    @Override
    @Transactional
    public PersonDto decreaseBalance(UUID personId, BigDecimal delta) {
        if (delta.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Некорректная сумма для вывода средств: " + delta);
        }

        var person = personRepository
                .findById(personId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        var newBalance = person.getMoney().subtract(delta);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Недостаточно средств для вывода");
        }

        person.setMoney(newBalance);
        person = personRepository.save(person);

        return personMapper.entityToAdminDto(person);
    }

}
