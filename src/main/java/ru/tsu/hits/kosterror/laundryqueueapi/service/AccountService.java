package ru.tsu.hits.kosterror.laundryqueueapi.service;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.PersonDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.StudentDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.UpdateAdminInfo;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.UpdateStudentInfo;

import java.util.UUID;

public interface AccountService {

    StudentDto getStudentInfo(UUID id);
    PersonDto getPersonInfo(UUID id);

    PersonDto changeAdminInfo(UUID id, UpdateAdminInfo updateAdminInfo);
    StudentDto changeStudentInfo(UUID id, UpdateStudentInfo updateStudentInfo);
}
