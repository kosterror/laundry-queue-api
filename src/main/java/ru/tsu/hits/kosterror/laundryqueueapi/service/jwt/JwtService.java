package ru.tsu.hits.kosterror.laundryqueueapi.service.jwt;

import org.springframework.lang.NonNull;
import ru.tsu.hits.kosterror.laundryqueueapi.security.PersonData;

import java.util.UUID;

public interface JwtService {
    String generateToken(@NonNull UUID id,
                         @NonNull String email,
                         @NonNull String role);

    PersonData decodeToken(@NonNull String token);
}
