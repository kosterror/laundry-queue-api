package ru.tsu.hits.kosterror.laundryqueueapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.person.CreateEmployeeDto;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.AccountStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.Role;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.BadRequestException;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.ConflictException;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.PersonRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.service.ManageAccountService;
import ru.tsu.hits.kosterror.laundryqueueapi.service.PasswordGenerator;

import java.math.BigDecimal;

import static ru.tsu.hits.kosterror.laundryqueueapi.util.ValidationConstants.EMAIL_REGEX;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManageAccountServiceImpl implements ManageAccountService {

    private final JavaMailSender mailSender;
    private final PasswordGenerator passwordGenerator;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String username;

    @Override
    @Transactional
    public void createStudent(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            throw new BadRequestException("Некорректный формат почты");
        }

        if (personRepository.existsByEmail(email)) {
            throw new ConflictException("Пользователь с такой почтой уже существует");
        }

        String password = passwordGenerator.generatePassword();
        buildAndSaveStudent(email, password);
        sendMessage(email, password);
        log.info("Студент с почтой {} успешно зарегистрирован", email);
    }

    @Override
    @Transactional
    public void createEmployee(CreateEmployeeDto dto) {
        if (personRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Пользователь с такой почтой уже существует");
        }

        String password = passwordGenerator.generatePassword();
        buildAndSaveEmployee(dto, password);
        sendMessage(dto.getEmail(), password);
        log.info("Сотрудник с почтой {} успешно зарегистрирован", dto.getEmail());
    }

    private void buildAndSaveStudent(String email, String password) {
        Person student = Person
                .builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name("")
                .surname("")
                .money(BigDecimal.ZERO)
                .status(AccountStatus.PENDING)
                .role(Role.ROLE_STUDENT)
                .build();

        personRepository.save(student);
    }

    private void buildAndSaveEmployee(CreateEmployeeDto dto, String password) {
        Person employee = Person
                .builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(password))
                .name(dto.getName())
                .surname(dto.getSurname())
                .money(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVATED)
                .role(Role.ROLE_EMPLOYEE)
                .build();

        personRepository.save(employee);
    }

    private void sendMessage(String email, String password) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(email);
        mailMessage.setSubject("Создан аккаунт для приложения LaundryQueue");
        mailMessage.setText("Вам создали аккаунт в приложении LaundryQueue. Для входа в аккаунт вам нужно " +
                "использовать почту, на которую пришло данное письмо и пароль: " + password);

        mailSender.send(mailMessage);
    }

}
