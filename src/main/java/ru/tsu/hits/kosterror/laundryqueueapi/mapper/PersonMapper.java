package ru.tsu.hits.kosterror.laundryqueueapi.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.PersonDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.StudentDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;

@Component
@RequiredArgsConstructor
public class PersonMapper {

    public StudentDto entityToStudentDto(Person entity){
        return new StudentDto(
                entity.getId(),
                entity.getDormitory() != null ? entity.getDormitory().getId() : null,
                entity.getEmail(),
                entity.getName(),
                entity.getSurname(),
                entity.getRoom(),
                entity.getMoney(),
                entity.getRole()
        );
    }
    public PersonDto entityToAdminDto(Person entity){
        return new PersonDto(
                entity.getId(),
                entity.getEmail(),
                entity.getName(),
                entity.getSurname(),
                entity.getMoney(),
                entity.getRole(),
                entity.getDormitory() != null ? entity.getDormitory().getId() : null
        );
    }

}
