package ru.tsu.hits.kosterror.laundryqueueapi.service.managemachine;

import ru.tsu.hits.kosterror.laundryqueueapi.entity.Dormitory;

import java.util.UUID;

public interface DormitoryService {

     Dormitory findDormitory(UUID dormitoryId);
}
