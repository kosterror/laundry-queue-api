package ru.tsu.hits.kosterror.laundryqueueapi.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.dormitory.DormitoryDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.PersonDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.StudentDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;

@Component
@RequiredArgsConstructor
public class PersonMapper {

    public StudentDto entityToStudentDto(Person entity) {
        return new StudentDto(
                entity.getId(),
                entity.getDormitory() != null ?
                        new DormitoryDto(entity.getDormitory().getId(), entity.getDormitory().getNumber()) : null,
                entity.getEmail(),
                entity.getName(),
                entity.getSurname(),
                entity.getRoom(),
                entity.getMoney(),
                entity.getStatus(),
                entity.getRole()
        );
    }

    public PersonDto entityToAdminDto(Person entity) {
        return new PersonDto(
                entity.getId(),
                entity.getEmail(),
                entity.getDormitory() != null ?
                        new DormitoryDto(entity.getDormitory().getId(), entity.getDormitory().getNumber()) : null,
                entity.getName(),
                entity.getSurname(),
                entity.getMoney(),
                entity.getRole(),
                entity.getDormitory() != null ? entity.getDormitory().getId() : null,
                entity.getStatus()
        );
    }

}
