package ru.tsu.hits.kosterror.laundryqueueapi.service.auth;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.ApiResponse;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.PersonCredentials;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.TokenDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.UnauthorizedException;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.PersonRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.service.jwt.JwtService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ApiResponse<TokenDto> login(@NonNull PersonCredentials credentials) {
        Person person = personRepository
                .findByEmail(credentials.getEmail())
                .orElseThrow(() -> new UnauthorizedException(2, "Не авторизован"));

        if (!passwordEncoder.matches(credentials.getPassword(), person.getPassword())) {
            throw new UnauthorizedException(2, "Не авторизован");
        }


        String accessToken = jwtService.generateToken(person.getId(), person.getEmail(), String.valueOf(person.getRole()));

        //TODO: добавить refresh токен

        return new ApiResponse<>(new TokenDto(
                accessToken,
                null
        ));
    }
}
