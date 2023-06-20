package ru.tsu.hits.kosterror.laundryqueueapi.dto.machine;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.queue.QueueSlotDto;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineDto {

    private UUID id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss:SSS")
    private LocalDateTime startTime;

    private MachineType type;

    private MachineStatus status;

    private List<QueueSlotDto> queueSlots;

    private UUID location;
}
