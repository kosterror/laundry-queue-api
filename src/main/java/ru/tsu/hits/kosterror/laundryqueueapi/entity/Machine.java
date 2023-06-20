package ru.tsu.hits.kosterror.laundryqueueapi.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineType;

import javax.persistence.*;
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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private LocalDateTime startTime;

    private String ip;

    @Enumerated(value = EnumType.STRING)
    private MachineType type;

    @Enumerated(value = EnumType.STRING)
    private MachineStatus status;

    @OneToMany(mappedBy = "machine", fetch = FetchType.EAGER)
    private List<QueueSlot> queueSlots;

    @ManyToOne
    @JoinColumn(name = "dormitory_id")
    private Dormitory location;

}
