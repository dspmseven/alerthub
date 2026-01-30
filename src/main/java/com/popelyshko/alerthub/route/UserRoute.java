package com.popelyshko.alerthub.route;

import com.popelyshko.alerthub.service.UserService;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:createUser")
                .routeId("create-user-route")
                .description("Create a new user")
                .setProperty("request", body())
                .setBody(simple("${body.username}"))
                .bean(UserService.class, "checkUserByUsername")
                .choice()
                    .when(simple("${body} == true"))
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(409))
                        .process(exchange -> {
                                    exchange.getMessage().setBody(Map.of("status", "failed", "response", "username is already exists")) ;
                        })
                        .marshal().json(JsonLibrary.Jackson).endChoice()
                    .otherwise()
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
                        .setBody(exchangeProperty("request"))
                        .bean(UserService.class, "createUser")
                        .process(exchange -> {
                            Long id = exchange.getMessage().getBody(Long.class);
                            exchange.getMessage().setBody(Map.of("status", "success", "response", "user created, with id - " + id));
                        })
                        .marshal().json(JsonLibrary.Jackson)
                .end();

        from("direct:getUserInfo")
                .routeId("get-user-info-route")
                .bean(UserService.class, "getUserByUsername")
                .marshal().json(JsonLibrary.Jackson);

        from("direct:generateTelegramCode")
                .routeId("generate-telegram-code")
                .setProperty("request", body())
                .bean(UserService.class, "checkUserByUsernameAndPassword")
                .choice()
                    .when(simple("${body} == true"))
                        .setBody(simple("${exchangeProperty.request.username}"))
                        .bean(UserService.class, "setTelegramLinkCodeToUser")
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                        .process(exchange -> {
                            String code = exchange.getMessage().getBody(String.class);
                            exchange.getMessage().setBody(Map.of("status", "success", "telegram", code));
                        })
                        .marshal().json(JsonLibrary.Jackson).endChoice()
                    .otherwise()
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(401))
                        .process(exchange -> {
                            exchange.getMessage().setBody(Map.of("status", "failed", "response", "invalid credentials"));
                        })
                        .marshal().json(JsonLibrary.Jackson).endChoice()
                .end();

    }
}
