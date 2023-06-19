package ru.tsu.hits.kosterror.laundryqueueapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByOwnerAndToken(Person owner, String token);

    Optional<RefreshToken> findByToken(String token);

}
