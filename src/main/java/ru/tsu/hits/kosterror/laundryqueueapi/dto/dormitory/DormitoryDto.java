package ru.tsu.hits.kosterror.laundryqueueapi.dto.dormitory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DormitoryDto {

    private UUID id;

    private Integer number;

}
