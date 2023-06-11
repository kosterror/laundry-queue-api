package ru.tsu.hits.kosterror.laundryqueueapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dormitory extends BaseEntity {

    private String name;

    private String address;

    @OneToMany(mappedBy = "homeDormitory")
    private List<Person> students;

    @OneToMany(mappedBy = "locationDormitory")
    private List<Machine> machines;

}
