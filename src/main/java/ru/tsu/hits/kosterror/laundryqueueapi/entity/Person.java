package ru.tsu.hits.kosterror.laundryqueueapi.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.Role;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person extends BaseEntity {

    private String studentNumber;

    private String name;

    private String surname;

    private String room;

    private BigDecimal moneyCount;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToOne
    private QueueSlot queueSlot;

    @ManyToOne
    @JoinColumn(name = "dormitory_id")
    private Dormitory homeDormitory;

    @OneToMany
    private List<Machine> machines;

}
