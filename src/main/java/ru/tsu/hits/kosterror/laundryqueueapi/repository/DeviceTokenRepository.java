package ru.tsu.hits.kosterror.laundryqueueapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.kosterror.laundryqueueapi.entity.DeviceToken;

import java.util.UUID;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, UUID> {
}
