package ru.tsu.hits.kosterror.laundryqueueapi.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.Role;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PersonData {
    private UUID id;
    private String email;
    private Role role;
}
