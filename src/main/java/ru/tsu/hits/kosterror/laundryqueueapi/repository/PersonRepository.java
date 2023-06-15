package ru.tsu.hits.kosterror.laundryqueueapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends CrudRepository<Person, UUID> {

    boolean existsByEmail(String email);

    Optional<Person> findByEmail(String email);

}
