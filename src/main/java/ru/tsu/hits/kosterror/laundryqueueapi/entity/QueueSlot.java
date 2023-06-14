package ru.tsu.hits.kosterror.laundryqueueapi.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private Integer number;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;

}
