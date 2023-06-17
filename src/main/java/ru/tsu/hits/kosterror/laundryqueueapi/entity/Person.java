package ru.tsu.hits.kosterror.laundryqueueapi.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.AccountStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.Role;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
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

    @OneToMany(mappedBy = "owner")
    private List<RefreshToken> refreshTokens;

    @ManyToOne
    @JoinColumn(name = "dormitory_id")
    private Dormitory dormitory;

}
