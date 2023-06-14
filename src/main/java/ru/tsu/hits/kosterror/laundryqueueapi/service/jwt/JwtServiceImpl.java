package ru.tsu.hits.kosterror.laundryqueueapi.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.Role;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.UnauthorizedException;
import ru.tsu.hits.kosterror.laundryqueueapi.security.PersonData;

import javax.annotation.PostConstruct;
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

    private Key key;

    @Value("${security.secret-key}")
    private String secretKey;

    @Value("${security.access-lifetime}")
    private int accessTokenLifetime;

    @PostConstruct
    private void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(@NonNull UUID id,
                                @NonNull String email,
                                @NonNull String role) {
        Date issuedAt = Date.from(Instant.now());
        Date expiresAt = Date.from(
                Instant.now()
                        .plus(
                                accessTokenLifetime,
                                ChronoUnit.MINUTES
                        )
        );

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
    public PersonData decodeToken(@NonNull String token) {
        try {
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
            throw new UnauthorizedException(2, "Не авторизован", exception);
        }
    }

}
