package ru.tsu.hits.kosterror.laundryqueueapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Machine;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.QueueSlot;

import java.util.List;
import java.util.UUID;

public interface QueueSlotRepository extends JpaRepository<QueueSlot, UUID> {
    List<QueueSlot> findAllByMachineOrderByNumberAsc(Machine machine);
}
