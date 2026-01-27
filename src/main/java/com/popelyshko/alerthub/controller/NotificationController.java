package com.popelyshko.alerthub.controller;

import com.popelyshko.alerthub.model.NotificationRequest;
import jakarta.validation.Valid;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    private final ProducerTemplate producerTemplate;

    public NotificationController(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @PostMapping
    public String sendNotification(@Valid @RequestBody NotificationRequest notificationRequest){
        producerTemplate.sendBody("direct:sendNotification", notificationRequest);
        return "notification sent successfully";
    }
}

