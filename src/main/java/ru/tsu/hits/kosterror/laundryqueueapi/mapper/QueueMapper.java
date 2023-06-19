package ru.tsu.hits.kosterror.laundryqueueapi.mapper;

import org.springframework.stereotype.Component;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.QueueSlotDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.QueueSlot;

@Component
public class QueueMapper {

    public QueueSlotDto entityToSlot(QueueSlot entity) {
        return new QueueSlotDto(
                entity.getId(),
                entity.getNumber(),
                entity.getPerson() != null ? entity.getPerson().getId() : null,
                entity.getPerson() != null
        );
    }

}
