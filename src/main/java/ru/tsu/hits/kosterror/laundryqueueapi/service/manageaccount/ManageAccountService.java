package ru.tsu.hits.kosterror.laundryqueueapi.service.manageaccount;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.CreateEmployeeDto;

public interface ManageAccountService {

    void createStudent(String email);

    void createEmployee(CreateEmployeeDto dto);

}
