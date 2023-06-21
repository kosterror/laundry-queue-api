package ru.tsu.hits.kosterror.laundryqueueapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Machine;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.QueueSlot;

import java.util.List;
import java.util.UUID;

@Repository
public interface QueueSlotRepository extends JpaRepository<QueueSlot, UUID> {
    List<QueueSlot> findAllByMachineOrderByNumberAsc(Machine machine);
}
