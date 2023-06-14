package ru.tsu.hits.kosterror.laundryqueueapi.service.manageaccount;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.Person;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.AccountStatus;
import ru.tsu.hits.kosterror.laundryqueueapi.enumeration.Role;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.EmailAlreadyUsedException;
import ru.tsu.hits.kosterror.laundryqueueapi.repository.PersonRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.service.passwordgenerator.PasswordGenerator;

import java.math.BigDecimal;

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
        if (personRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException(1, "Пользователь с такой почтой уже существует");
        }

        String password = passwordGenerator.generatePassword();
        buildAndSaveStudent(email, password);
        sendMessage(email, password);
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
                .role(Role.STUDENT)
                .build();

        personRepository.save(student);
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
