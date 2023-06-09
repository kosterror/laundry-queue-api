package ru.tsu.hits.kosterror.laundryqueueapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.dormitory.DormitoryDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Dormitory;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.mapper.DormitoryMapper;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.DormitoryRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.service.DormitoryService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DormitoryServiceImpl implements DormitoryService {

    private final DormitoryRepository dormitoryRepository;
    private final DormitoryMapper dormitoryMapper;

    @Override
    public Dormitory findDormitory(UUID dormitoryId) {

        return dormitoryRepository.findById(dormitoryId).orElseThrow(
                () -> new NotFoundException("Такого общежития найдено не было"));
    }

    @Override
    public List<DormitoryDto> getDormitories() {
        return dormitoryRepository
                .findAll(Sort.by(Sort.Direction.ASC, "number"))
                .stream()
                .map(dormitoryMapper::entityToDto)
                .toList();
    }
}
