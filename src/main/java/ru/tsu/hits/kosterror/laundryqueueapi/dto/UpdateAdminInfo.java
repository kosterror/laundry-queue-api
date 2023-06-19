package ru.tsu.hits.kosterror.laundryqueueapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

import static ru.tsu.hits.kosterror.laundryqueueapi.util.ValidationConstants.EMAIL_REGEX;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAdminInfo {

    @Schema(example = "email@domain.com")
    @Pattern(regexp = EMAIL_REGEX, message = "Почта не соответствует формату")
    private String email;

    @NotNull(message = "Имя не может быть null")
    private String name;

    @NotNull(message = "Фамилия не может быть null")
    private String surname;

    @NotNull(message = "Id общежития не может быть null")
    private UUID dormitoryId;
}
