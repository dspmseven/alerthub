package com.popelyshko.alerthub.route;

import com.popelyshko.alerthub.service.TelegramService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TelegramRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("telegram:bots?authorizationToken={{TELEGRAM_AUTH_TOKEN}}")
                .routeId("telegram-user-check-route")
                .description("Check telegram user")
                .bean(TelegramService.class, "checkTelegramToken")
                .to("telegram:bots?authorizationToken={{TELEGRAM_AUTH_TOKEN}}");
    }
}
