package com.popelyshko.alerthub.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.TelegramMediaType;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class NotificationRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(Exception.class)
                .log(LoggingLevel.ERROR, "Exception occurred: ${exception.message}")
                .handled(true);

        from("direct:sendNotification")
                .routeId("notification-save-route")
                .description("Save notification in database and send to JMS queue")
                .log(LoggingLevel.INFO, "Get notification ${body}")
                .to("bean:notificationService?method=saveNotification")
                .setHeader("severity", simple("${body.severity}"))
                .marshal().json(JsonLibrary.Jackson)
                .choice()
                .when(header("severity").isEqualTo("CRITICAL"))
                    .to("jms:queue:notification.critical")
                .when(header("severity").isEqualTo("HIGH"))
                    .to("jms:queue:notification.high")
                .when(header("severity").isEqualTo("MEDIUM"))
                    .to("jms:queue:notification.medium")
                .when(header("severity").isEqualTo("LOW"))
                    .to("jms:queue:notification.low")
                .otherwise()
                        .log("Unknown severity type")
                        .to("jms:queue:DLQ")
                .end();

        from("jms:queue:notification.critical")
                .routeId("notification-consume-route")
                .convertBodyTo(String.class)
                .setHeader("CamelTelegramMediaType" , constant(TelegramMediaType.TEXT))
                .to("telegram:bots?authorizationToken={{TELEGRAM_AUTH_TOKEN}}");
    }
}

