package ru.tsu.hits.kosterror.laundryqueueapi.repository;

import org.springframework.data.repository.CrudRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;

import java.util.UUID;

public interface PersonRepository extends CrudRepository<Person, UUID> {

    boolean existsByEmail(String email);

}
