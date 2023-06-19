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

    @NotNull(message = "Тип машины не может быть null")
    private MachineType type;

    @NotNull(message = "Статус машины не может быть null")
    private MachineStatus machineStatus;

    @NotNull(message = "Ip машины не может быть null")
    private String ip;

    @NotNull(message = "Местоположение машины не может быть null")
    private UUID location;

}
