package ru.tsu.hits.kosterror.laundryqueueapi.dto.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.dormitory.DormitoryDto;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.AccountStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.Role;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private UUID id;
    private String email;
    private DormitoryDto dormitoryDto;
    private String name;
    private String surname;
    private BigDecimal money;
    private Role role;
    private UUID dormitoryId;
    private AccountStatus accountStatus;
}
