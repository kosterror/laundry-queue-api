package ru.tsu.hits.kosterror.laundryqueueapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "machine")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Machine {

    @Id
    @UuidGenerator
    private UUID id;

    private LocalDateTime startTime;

    @Enumerated(value = EnumType.STRING)
    private MachineType type;

    @Enumerated(value = EnumType.STRING)
    private MachineStatus status;

    @OneToMany(mappedBy = "machine")
    private List<QueueSlot> queueSlots;

    @ManyToOne
    @JoinColumn(name = "dormitory_id")
    private Dormitory location;

}
