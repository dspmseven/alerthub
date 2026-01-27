package com.popelyshko.alerthub.repository;

import com.popelyshko.alerthub.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
