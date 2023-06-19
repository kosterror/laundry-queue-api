package ru.tsu.hits.kosterror.laundryqueueapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
    private String accessToken;
    private String refreshToken;
    private Role role;
}
