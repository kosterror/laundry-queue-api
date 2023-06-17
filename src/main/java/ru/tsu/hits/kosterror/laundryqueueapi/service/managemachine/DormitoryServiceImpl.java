package ru.tsu.hits.kosterror.laundryqueueapi.service.managemachine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Dormitory;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.DormitoryRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DormitoryServiceImpl implements DormitoryService {

    private final DormitoryRepository dormitoryRepository;

    @Override
    public Dormitory findDormitory(UUID dormitoryId) {

        return dormitoryRepository.findById(dormitoryId).orElseThrow(
                () -> new NotFoundException("Такого общежития найдено не было"));
    }
}
