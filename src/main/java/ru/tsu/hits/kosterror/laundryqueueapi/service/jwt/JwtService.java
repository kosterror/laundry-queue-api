package ru.tsu.hits.kosterror.laundryqueueapi.service.jwt;

import org.springframework.lang.NonNull;
import ru.tsu.hits.kosterror.laundryqueueapi.security.PersonData;

import java.util.UUID;

public interface JwtService {
    String generateAccessToken(@NonNull UUID id,
                               @NonNull String email,
                               @NonNull String role);

    String generateRefreshToken(@NonNull UUID id,
                                @NonNull String email,
                                @NonNull String role);

    PersonData decodeAccessToken(@NonNull String token);
}
