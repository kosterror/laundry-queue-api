package ru.tsu.hits.kosterror.laundryqueueapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.SlotStatus;

import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueSlotDto implements Comparable<QueueSlotDto> {

    @Schema(description = "Идентификатор слота очереди")
    private UUID id;

    @Schema(description = "Номер слота очереди")
    private Integer number;

    @Schema(description = "Идентификатор пользователя, который находится в этом слоте")
    private UUID personId;

    @Schema(description = "Состояние слота")
    private SlotStatus status;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueueSlotDto that = (QueueSlotDto) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(number, that.number)) return false;
        if (!Objects.equals(personId, that.personId)) return false;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (personId != null ? personId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(@NonNull QueueSlotDto object) {
        int objectNumber = object.getNumber();

        if (number < objectNumber) {
            return -1;
        } else if (number > objectNumber) {
            return 1;
        }

        return 0;
    }
}
