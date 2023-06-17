package ru.tsu.hits.kosterror.laundryqueueapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.MachineType;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewMachineDto {

    @NotNull
    private MachineType type;

    @NotNull
    private MachineStatus machineStatus;

    @NotNull
    private UUID location;
}
