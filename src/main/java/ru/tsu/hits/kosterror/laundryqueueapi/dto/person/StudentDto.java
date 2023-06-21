package ru.tsu.hits.kosterror.laundryqueueapi.dto.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.AccountStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.Role;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDto {

    private UUID id;

    private UUID dormitoryId;

    private String email;

    private String name;

    private String surname;

    private String room;

    private BigDecimal money;

    private AccountStatus accountStatus;

    private Role role;
}
