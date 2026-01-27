package com.popelyshko.alerthub.route;

import com.popelyshko.alerthub.model.Notification;
import com.popelyshko.alerthub.model.NotificationRequest;
import com.popelyshko.alerthub.service.NotificationService;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class NotificationRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:sendNotification")
                .log(LoggingLevel.INFO, "Sending notification")
                .bean(NotificationService.class, "saveNotification")
                .marshal().json(JsonLibrary.Jackson, Notification.class)
                .to("jms:queue:notification");
    }
}
