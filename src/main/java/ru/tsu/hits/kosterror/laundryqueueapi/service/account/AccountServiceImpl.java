package ru.tsu.hits.kosterror.laundryqueueapi.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.PersonDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.StudentDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.UpdateAdminInfo;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.UpdateStudentInfo;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.mapper.PersonMapper;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.PersonRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public PersonDto getPersonInfo(UUID id){

        return personMapper.entityToDto(findPerson(id));
    }

    @Override
    public PersonDto changeAdminInfo(UUID id, UpdateAdminInfo updateAdminInfo) {
       Person person = findPerson(id);

        person.setEmail(updateAdminInfo.getEmail());
        person.setName(updateAdminInfo.getName());
        person.setSurname(updateAdminInfo.getSurname());

        personRepository.save(person);
        return personMapper.entityToDto(person);
    }

    @Override
    public StudentDto changeStudentInfo(UUID id, UpdateStudentInfo updateStudentInfo) {
        Person person = findPerson(id);

        person.setEmail(updateStudentInfo.getEmail());
        person.setName(updateStudentInfo.getName());
        person.setSurname(updateStudentInfo.getSurname());
        person.setRoom(updateStudentInfo.getRoom());
        person.setStudentNumber(updateStudentInfo.getStudentNumber());

        personRepository.save(person);
        return personMapper.entityToStudentDto(person);
    }

    private Person findPerson(UUID id){
       return personRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
