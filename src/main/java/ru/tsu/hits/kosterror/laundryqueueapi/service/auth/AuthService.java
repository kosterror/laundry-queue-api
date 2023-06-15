package ru.tsu.hits.kosterror.laundryqueueapi.service.auth;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.ApiResponse;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.PersonCredentials;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.StringObject;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.TokenDto;

import java.util.UUID;

public interface AuthService {

    ApiResponse<TokenDto> login(PersonCredentials credentials);

    ApiResponse<StringObject> logout(UUID id, StringObject refreshToken);

}
