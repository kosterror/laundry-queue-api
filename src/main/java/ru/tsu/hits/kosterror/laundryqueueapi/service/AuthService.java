package ru.tsu.hits.kosterror.laundryqueueapi.service;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.api.StringObject;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.AuthDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.PersonCredentials;

import java.util.UUID;

public interface AuthService {

    AuthDto login(PersonCredentials credentials);

    void logout(UUID id, StringObject refreshToken);

    AuthDto refreshTokens(StringObject refreshToken);
}
