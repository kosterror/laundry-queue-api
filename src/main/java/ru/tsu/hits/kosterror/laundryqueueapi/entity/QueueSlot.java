package ru.tsu.hits.kosterror.laundryqueueapi.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.SlotStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "queue_slot")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueueSlot implements Comparable<QueueSlot> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private Integer number;

    private LocalDateTime statusChanged;

    @Enumerated(value = EnumType.STRING)
    private SlotStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    private Person person;

    @Override
    public int compareTo(@NonNull QueueSlot object) {
        int objectNumber = object.getNumber();

        if (number < objectNumber) {
            return -1;
        } else if (number > objectNumber) {
            return 1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueueSlot queueSlot = (QueueSlot) o;

        return Objects.equals(id, queueSlot.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
