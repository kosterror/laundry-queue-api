package ru.tsu.hits.kosterror.laundryqueueapi.service.token;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.DeviceTokenDto;

public interface TokenService {

    void createNewToken(DeviceTokenDto deviceTokenDto);
}
