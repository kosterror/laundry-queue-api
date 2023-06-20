package ru.tsu.hits.kosterror.laundryqueueapi.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.Role;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.UnauthorizedException;
import ru.tsu.hits.kosterror.laundryqueueapi.security.PersonData;
import ru.tsu.hits.kosterror.laundryqueueapi.service.JwtService;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private static final String ISSUER = "laundry-queue-api";
    private static final String CLAIM_ID = "id";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_ROLE = "role";
    @Value("${security.access-secret-key}")
    private String accessSecret;

    @Value("${security.refresh-secret-key}")
    private String refreshSecret;

    @Value("${security.access-lifetime-min}")
    private int accessTokenLifetime;

    @Value("${security.refresh-lifetime-days}")
    private int refreshTokenLifetime;

    @Override
    public String generateAccessToken(@NonNull UUID id,
                                      @NonNull String email,
                                      @NonNull String role) {
        Key key = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        Date expiresAt = Date.from(
                Instant.now()
                        .plus(
                                accessTokenLifetime,
                                ChronoUnit.MINUTES
                        )
        );

        return generateToken(id, email, role, expiresAt, key);
    }

    @Override
    public Pair<String, Date> generateRefreshTokenAndExpiresDate(@NonNull UUID id,
                                                                 @NonNull String email,
                                                                 @NonNull String role) {
        Key key = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
        Date expiresAt = Date.from(
                Instant.now()
                        .plus(
                                refreshTokenLifetime,
                                ChronoUnit.DAYS
                        )
        );

        return Pair.of(generateToken(id, email, role, expiresAt, key), expiresAt);
    }

    private String generateToken(UUID id,
                                 String email,
                                 String role,
                                 Date expiresAt,
                                 Key key) {
        Date issuedAt = Date.from(Instant.now());

        return Jwts
                .builder()
                .setIssuer(ISSUER)
                .setSubject(id.toString())
                .claim(CLAIM_ID, id.toString())
                .claim(CLAIM_ROLE, role)
                .claim(CLAIM_EMAIL, email)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public PersonData decodeAccessToken(@NonNull String token) {
        try {
            Key key = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
            Jws<Claims> data = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = data.getBody();

            return new PersonData(
                    UUID.fromString(claims.getSubject()),
                    claims.get(CLAIM_EMAIL, String.class),
                    Role.valueOf(claims.get(CLAIM_ROLE, String.class))
            );
        } catch (Exception exception) {
            throw new UnauthorizedException("Не авторизован", exception);
        }
    }

}
