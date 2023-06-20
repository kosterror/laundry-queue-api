package ru.tsu.hits.kosterror.laundryqueueapi.service;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.CreateEmployeeDto;

public interface ManageAccountService {

    void createStudent(String email);

    void createEmployee(CreateEmployeeDto dto);

}
