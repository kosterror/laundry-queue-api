package ru.tsu.hits.kosterror.laundryqueueapi.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Machine extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "dormitory_id")
    private Dormitory locationDormitory;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Person owner;

    @Enumerated(value = EnumType.STRING)
    private MachineType type;

    @Enumerated(value = EnumType.STRING)
    private MachineStatus status;

    private LocalDateTime startTime;

    @OneToMany
    private List<QueueSlot> queue;

}
