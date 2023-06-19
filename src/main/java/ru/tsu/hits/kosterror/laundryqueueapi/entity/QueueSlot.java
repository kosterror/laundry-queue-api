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
public class QueueSlot implements Comparable {

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

    @Override
    public int compareTo(@NonNull Object object) {
        if (object.getClass() != QueueSlot.class) {
            throw new ClassCastException("Некорректный тип у сравниваемого объекта");
        }
        int oNumber = ((QueueSlot) object).getNumber();

        if (number < oNumber) {
            return -1;
        } else if (number > oNumber) {
            return 1;
        }

        return 0;
    }

}
