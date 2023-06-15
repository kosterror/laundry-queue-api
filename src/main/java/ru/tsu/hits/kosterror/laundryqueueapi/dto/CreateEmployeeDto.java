package ru.tsu.hits.kosterror.laundryqueueapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static ru.tsu.hits.kosterror.laundryqueueapi.util.ValidationConstants.EMAIL_REGEX;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeDto {

    @NotNull
    @Pattern(regexp = EMAIL_REGEX)
    String email;

    @NotNull
    String name;

    @NotNull
    String surname;

}
