package ru.tsu.hits.kosterror.laundryqueueapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

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
    @UuidGenerator
    private UUID id;

    private Integer number;

    @OneToOne
    @JoinColumn(name = "director_id")
    private Person director;

    @OneToMany(mappedBy = "location")
    private List<Machine> machines;

}
