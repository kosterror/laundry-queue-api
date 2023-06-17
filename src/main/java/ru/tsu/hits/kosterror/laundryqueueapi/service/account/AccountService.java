package ru.tsu.hits.kosterror.laundryqueueapi.service.account;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.PersonDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.StudentDto;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.UpdateAdminInfo;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.UpdateStudentInfo;

import java.util.UUID;

public interface AccountService {

    PersonDto getPersonInfo(UUID id);

    PersonDto changeAdminInfo(UUID id, UpdateAdminInfo updateAdminInfo);
    StudentDto changeStudentInfo(UUID id, UpdateStudentInfo updateStudentInfo);
}
