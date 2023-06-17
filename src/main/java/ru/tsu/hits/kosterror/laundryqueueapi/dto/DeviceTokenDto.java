package ru.tsu.hits.kosterror.laundryqueueapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceTokenDto {

    @NotNull
    private String token;
}
