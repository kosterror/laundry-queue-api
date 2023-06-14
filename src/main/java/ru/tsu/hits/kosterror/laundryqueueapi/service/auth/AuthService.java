package ru.tsu.hits.kosterror.laundryqueueapi.service.auth;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.ApiResponse;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.PersonCredentials;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.TokenDto;

public interface AuthService {

    ApiResponse<TokenDto> login(PersonCredentials credentials);

}
