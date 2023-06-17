package ru.tsu.hits.kosterror.laundryqueueapi.repository;

import org.springframework.data.repository.CrudRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Dormitory;

import java.util.UUID;

public interface DormitoryRepository extends CrudRepository<Dormitory, UUID> {
}
