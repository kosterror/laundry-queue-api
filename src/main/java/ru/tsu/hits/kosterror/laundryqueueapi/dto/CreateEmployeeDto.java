package ru.tsu.hits.kosterror.laundryqueueapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "email@domain.com")
    @Pattern(regexp = EMAIL_REGEX, message = "Почта не соответствует формату")
    String email;

    @NotNull(message = "Имя не может быть null")
    String name;

    @NotNull(message = "Фамилия не может быть null")
    String surname;

}
