package ru.tsu.hits.kosterror.laundryqueueapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.AccountStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.Role;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {

    @Id
    @UuidGenerator
    private UUID id;

    private String studentNumber;

    private String email;

    private String password;

    private String name;

    private String surname;

    private String room;

    private BigDecimal money;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus status;

    @OneToOne(mappedBy = "person")
    private QueueSlot queueSlot;

}
