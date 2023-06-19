package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.QueueSlotDto;
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

}
