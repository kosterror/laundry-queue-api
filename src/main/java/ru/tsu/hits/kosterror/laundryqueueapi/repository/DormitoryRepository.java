package ru.tsu.hits.kosterror.laundryqueueapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Dormitory;

import java.util.UUID;

@Repository
public interface DormitoryRepository extends JpaRepository<Dormitory, UUID> {
}
