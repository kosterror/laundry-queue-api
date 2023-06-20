package ru.tsu.hits.kosterror.laundryqueueapi.service.dormitory;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.dormitory.DormitoryDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Dormitory;

import java.util.List;
import java.util.UUID;

public interface DormitoryService {

     Dormitory findDormitory(UUID dormitoryId);

     List<DormitoryDto> getDormitories();
}
