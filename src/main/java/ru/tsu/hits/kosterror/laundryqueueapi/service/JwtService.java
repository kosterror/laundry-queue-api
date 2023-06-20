package ru.tsu.hits.kosterror.laundryqueueapi.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.lang.NonNull;
import ru.tsu.hits.kosterror.laundryqueueapi.security.PersonData;

import java.util.Date;
import java.util.UUID;

public interface JwtService {
    String generateAccessToken(@NonNull UUID id,
                               @NonNull String email,
                               @NonNull String role);

    Pair<String, Date> generateRefreshTokenAndExpiresDate(@NonNull UUID id,
                                                          @NonNull String email,
                                                          @NonNull String role);

    PersonData decodeAccessToken(@NonNull String token);
}
