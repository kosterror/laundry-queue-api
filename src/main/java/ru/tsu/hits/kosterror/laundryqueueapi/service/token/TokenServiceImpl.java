package ru.tsu.hits.kosterror.laundryqueueapi.service.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.DeviceTokenDto;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.PersonRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final PersonRepository personRepository;

    @Override
    public void saveDeviceToken(UUID personId, DeviceTokenDto deviceTokenDto) {
        var person = personRepository
                .findById(personId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %s не найден", personId)));

        person.setDeviceToken(deviceTokenDto.getToken());
        personRepository.save(person);
    }

}
