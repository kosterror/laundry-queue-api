package ru.tsu.hits.kosterror.laundryqueueapi.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dormitory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dormitory {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private Integer number;

    @OneToOne
    @JoinColumn(name = "director_id")
    private Person director;

    @OneToMany(mappedBy = "location")
    private List<Machine> machines;

}
