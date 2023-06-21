package ru.tsu.hits.kosterror.laundryqueueapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.PersonDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.StudentDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.UpdateAdminInfo;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.UpdateStudentInfo;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.AccountStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.NotFoundException;
import ru.tsu.hits.kosterror.laundryqueueapi.mapper.PersonMapper;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.PersonRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.service.AccountService;
import ru.tsu.hits.kosterror.laundryqueueapi.service.DormitoryService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final DormitoryService dormitoryService;

    @Override
    public StudentDto getStudentInfo(UUID id){

        return personMapper.entityToStudentDto(findPerson(id));
    }

    @Override
    public PersonDto getPersonInfo(UUID id) {
        return personMapper.entityToAdminDto(findPerson(id));
    }

    @Override
    public PersonDto changeAdminInfo(UUID id, UpdateAdminInfo updateAdminInfo) {
       Person person = findPerson(id);

        person.setEmail(updateAdminInfo.getEmail());
        person.setName(updateAdminInfo.getName());
        person.setSurname(updateAdminInfo.getSurname());
        person.setDormitory(dormitoryService.findDormitory(updateAdminInfo.getDormitoryId()));

        personRepository.save(person);
        return personMapper.entityToAdminDto(person);
    }

    @Override
    public StudentDto changeStudentInfo(UUID id, UpdateStudentInfo updateStudentInfo) {
        Person person = findPerson(id);

        person.setEmail(updateStudentInfo.getEmail());
        person.setName(updateStudentInfo.getName());
        person.setSurname(updateStudentInfo.getSurname());
        person.setRoom(updateStudentInfo.getRoom());
        person.setDormitory(dormitoryService.findDormitory(updateStudentInfo.getDormitoryId()));
        person.setStatus(AccountStatus.ACTIVATED);
        personRepository.save(person);
        return personMapper.entityToStudentDto(person);
    }

    private Person findPerson(UUID id){
       return personRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
