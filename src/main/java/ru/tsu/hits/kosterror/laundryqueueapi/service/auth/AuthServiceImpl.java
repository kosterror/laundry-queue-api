package ru.tsu.hits.kosterror.laundryqueueapi.service.auth;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.PersonCredentials;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.StringObject;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.TokenDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.RefreshToken;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.BadRequestException;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.UnauthorizedException;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.PersonRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.RefreshTokenRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.service.jwt.JwtService;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public TokenDto login(@NonNull PersonCredentials credentials) {
        Person person = personRepository
                .findByEmail(credentials.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Не авторизован"));

        if (!passwordEncoder.matches(credentials.getPassword(), person.getPassword())) {
            throw new UnauthorizedException("Не авторизован");
        }

        String accessToken = jwtService.generateAccessToken(person.getId(), person.getEmail(), String.valueOf(person.getRole()));

        return new TokenDto(
                accessToken,
                generateAndSaveRefreshToken(person),
                person.getRole()
        );
    }

    @Override
    public void logout(UUID id, StringObject inputRefreshToken) {
        Person owner = personRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не найден"));

        RefreshToken refreshToken = refreshTokenRepository
                .findByOwnerAndToken(owner, inputRefreshToken.getValue())
                .orElseThrow(() -> new NotFoundException("Такой рефреш токен не найден"));

        refreshTokenRepository.delete(refreshToken);

        if (refreshToken.getExpiredAt().before(new Date())) {
            throw new BadRequestException("Срок действия рефреш токена истёк");
        }
    }

    private String generateAndSaveRefreshToken(Person person) {
        var refreshTokenAndExpiresDate = jwtService.generateRefreshTokenAndExpiresDate(person.getId(),
                person.getEmail(),
                person.getRole().toString()
        );

        RefreshToken refreshTokenEntity = RefreshToken
                .builder()
                .expiredAt(refreshTokenAndExpiresDate.getRight())
                .token(refreshTokenAndExpiresDate.getLeft())
                .owner(person)
                .build();

        refreshTokenEntity = refreshTokenRepository.save(refreshTokenEntity);
        return refreshTokenEntity.getToken();
    }

}
