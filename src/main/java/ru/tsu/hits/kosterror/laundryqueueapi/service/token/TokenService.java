package ru.tsu.hits.kosterror.laundryqueueapi.service.token;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.api.DeviceTokenDto;

import java.util.UUID;

public interface TokenService {
    void saveDeviceToken(UUID personId, DeviceTokenDto deviceTokenDto);
}
