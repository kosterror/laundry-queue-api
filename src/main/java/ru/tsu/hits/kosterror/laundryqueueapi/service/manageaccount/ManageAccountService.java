package ru.tsu.hits.kosterror.laundryqueueapi.service.manageaccount;

import ru.tsu.hits.kosterror.laundryqueueapi.dto.ApiResponse;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.StringObject;

public interface ManageAccountService {

    ApiResponse<StringObject> createStudent(String email);

}
