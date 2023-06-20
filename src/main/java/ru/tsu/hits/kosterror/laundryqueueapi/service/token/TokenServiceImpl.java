package ru.tsu.hits.kosterror.laundryqueueapi.service.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.api.DeviceTokenDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.DeviceToken;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.DeviceTokenRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.PersonRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final PersonRepository personRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    @Transactional
    @Override
    public void saveDeviceToken(UUID personId, DeviceTokenDto deviceTokenDto) {
        var person = personRepository
                .findById(personId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %s не найден", personId)));

        var deviceToken = DeviceToken
                .builder()
                .createdDate(LocalDateTime.now())
                .owner(person)
                .token(deviceTokenDto.getToken())
                .build();

        deviceTokenRepository.save(deviceToken);
    }

}
