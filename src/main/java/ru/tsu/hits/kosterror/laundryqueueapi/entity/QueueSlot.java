package ru.tsu.hits.kosterror.laundryqueueapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "queue_slot")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueueSlot {

    @Id
    @UuidGenerator
    private UUID id;

    private Integer number;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;

}
