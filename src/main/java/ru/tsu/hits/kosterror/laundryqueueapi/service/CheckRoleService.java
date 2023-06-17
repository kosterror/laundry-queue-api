package ru.tsu.hits.kosterror.laundryqueueapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.Role;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.ForbiddenException;
import ru.tsu.hits.kosterror.laundryqueueapi.security.PersonData;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckRoleService {

    public void checkAdminRole(PersonData personData){
        if (personData.getRole() != Role.ROLE_ADMIN) {
            throw new ForbiddenException("У вас нет доступа");
        }
    }

}
