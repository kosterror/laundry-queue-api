package ru.tsu.hits.kosterror.laundryqueueapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.dormitory.DormitoryDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Dormitory;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DormitoryMapper {

    DormitoryDto entityToDto(Dormitory dormitory);
}
