package ru.tsu.hits.kosterror.laundryqueueapi.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.DeviceTokenDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.DeviceToken;

@Component
@RequiredArgsConstructor
public class TokenMapper {
    public DeviceToken tokenDtoToEntity(DeviceTokenDto deviceTokenDto) {
        return DeviceToken
                .builder()
                .token(deviceTokenDto.getToken())
                .build();
    }
}
