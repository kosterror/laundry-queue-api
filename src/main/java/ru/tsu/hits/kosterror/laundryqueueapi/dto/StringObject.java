package ru.tsu.hits.kosterror.laundryqueueapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StringObject {
    @NotNull(message = "Строка не может быть равна null")
    private String value;
}
