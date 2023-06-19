package ru.tsu.hits.kosterror.laundryqueueapi.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.StudentDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    public StudentDto entityToStudentDto(Person entity){
        return new StudentDto(
                entity.getId(),
                entity.getDormitory().getId(),
                entity.getEmail(),
                entity.getName(),
                entity.getSurname(),
                entity.getRoom(),
                entity.getMoney(),
                entity.getRole()
        );
    }

}
