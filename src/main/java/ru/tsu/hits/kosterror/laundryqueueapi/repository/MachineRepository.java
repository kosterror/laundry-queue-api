package ru.tsu.hits.kosterror.laundryqueueapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Dormitory;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Machine;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineStatus;

import java.util.List;
import java.util.UUID;

@Repository
public interface MachineRepository extends JpaRepository<Machine, UUID> {

    List<Machine> findAllByStatus(MachineStatus status);

    List<Machine> findAllByLocationOrderByNameAsc(Dormitory dormitory);

}
