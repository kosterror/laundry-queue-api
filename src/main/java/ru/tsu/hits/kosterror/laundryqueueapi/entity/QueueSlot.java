package ru.tsu.hits.kosterror.laundryqueueapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueSlot extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;

    private Integer number;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;

}
