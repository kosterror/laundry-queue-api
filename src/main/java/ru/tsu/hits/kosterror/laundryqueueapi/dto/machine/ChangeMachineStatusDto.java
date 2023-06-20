package ru.tsu.hits.kosterror.laundryqueueapi.dto.machine;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineStatus;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeMachineStatusDto {

    @NotNull
    private UUID machineId;

    @NotNull
    private MachineStatus status;
}
