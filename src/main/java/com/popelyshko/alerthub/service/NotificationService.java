package com.popelyshko.alerthub.service;

import com.popelyshko.alerthub.model.Notification;
import com.popelyshko.alerthub.model.NotificationRequest;
import com.popelyshko.alerthub.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification saveNotification(NotificationRequest notificationRequest) {
        log.info("Saving notification {}", notificationRequest);
        Notification notification = new Notification();
        notification.setName(notificationRequest.getName());
        notification.setMessage(notificationRequest.getMessage());
        notification.setDate(notificationRequest.getDate());
        notification.setSeverity(notificationRequest.getSeverity());
        notification.setSource(notificationRequest.getSource());
        notification.setTags(notificationRequest.getTags());
        notification.setResolved(notificationRequest.isResolved());
        return notificationRepository.save(notification);
    }
}
