package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.QueueSlotDto;
import ru.tsu.hits.kosterror.laundryqueueapi.security.PersonData;
import ru.tsu.hits.kosterror.laundryqueueapi.service.queue.QueueService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Очередь")
public class QueueController {

    private final QueueService queueService;

    @Operation(
            summary = "Получить очередь конкретной машины",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/queue/{machineId}")
    public List<QueueSlotDto> getQueue(@PathVariable UUID machineId) {
        return queueService.getQueueByMachine(machineId);
    }

    @Operation(
            summary = "Начать стирку",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/laundry/start")
    public void startLaundry(Authentication authentication) {
        queueService.startLaundry(((PersonData) authentication.getPrincipal()).getId());
    }

    @Operation(
            summary = "Записаться в слот",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/queue/slots/{slotId}")
    public List<QueueSlotDto> signUpForQueue(@PathVariable UUID slotId,
                                             Authentication authentication) {
        return queueService.signUpForQueue(
                ((PersonData) authentication.getPrincipal()).getId(),
                slotId
        );
    }


}
