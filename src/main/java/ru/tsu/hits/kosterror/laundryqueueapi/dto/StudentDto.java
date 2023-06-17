package ru.tsu.hits.kosterror.laundryqueueapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDto {

    private UUID id;

    private UUID dormitoryId;

    private String studentNumber;

    private String email;

    private String name;

    private String surname;

    private String room;

    private BigDecimal money;
}
