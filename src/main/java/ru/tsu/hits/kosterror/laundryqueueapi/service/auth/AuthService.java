package ru.tsu.hits.kosterror.laundryqueueapi.service.auth;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.AuthDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.PersonCredentials;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.StringObject;

import java.util.UUID;

public interface AuthService {

    AuthDto login(PersonCredentials credentials);

    void logout(UUID id, StringObject refreshToken);

    AuthDto refreshTokens(UUID personId, StringObject refreshToken);
}
