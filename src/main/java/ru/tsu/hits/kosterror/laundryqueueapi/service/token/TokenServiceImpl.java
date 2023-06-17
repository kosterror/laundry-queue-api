package ru.tsu.hits.kosterror.laundryqueueapi.service.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.DeviceTokenDto;
import ru.tsu.hits.kosterror.laundryqueueapi.mapper.TokenMapper;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.DeviceTokenRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{

    private final DeviceTokenRepository deviceTokenRepository;
    private final TokenMapper tokenMapper;

    @Override
    public void createNewToken(DeviceTokenDto deviceTokenDto) {

        deviceTokenRepository.save(tokenMapper.tokenDtoToEntity(deviceTokenDto));
    }
}
