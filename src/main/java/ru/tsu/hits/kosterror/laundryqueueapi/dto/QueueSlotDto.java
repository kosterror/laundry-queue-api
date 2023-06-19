package ru.tsu.hits.kosterror.laundryqueueapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueSlotDto {

    @Schema(description = "Идентификатор слота очереди")
    private UUID id;

    @Schema(description = "Номер слота очереди")
    private Integer number;

    @Schema(description = "Идентификатор пользователя, который находится в этом слоте")
    private UUID personId;

    @Schema(description = "Занят ли слот кем-то")
    private Boolean isBusy;

}
