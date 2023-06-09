package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.dormitory.DormitoryDto;
import ru.tsu.hits.kosterror.laundryqueueapi.service.DormitoryService;

import java.util.List;

@RestController
@RequestMapping("/api/dormitories")
@RequiredArgsConstructor
@Tag(name = "Общежития")
public class DormitoryController {

    private final DormitoryService dormitoryService;

    @Operation(
            summary = "Получить данные об общежитиях."
    )
    @GetMapping
    public List<DormitoryDto> getDormitoryDto() {
        return dormitoryService.getDormitories();
    }
}
