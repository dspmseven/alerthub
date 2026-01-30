package com.popelyshko.alerthub.controller;

import com.popelyshko.alerthub.model.GenerateTelegramLinkRequest;
import com.popelyshko.alerthub.model.UserRequest;
import jakarta.validation.Valid;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final ProducerTemplate producerTemplate;

    public UserController(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRequest userRequest) {
        Exchange response = producerTemplate.request("direct:createUser", exchange -> {
            exchange.getMessage().setBody(userRequest);
        });
        String body = response.getMessage().getBody(String.class);
        Integer httpCode =  response.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);

        return ResponseEntity.status(httpCode != null ? httpCode : 200).body(body);
    }

    @GetMapping(value = "/{username}/info", produces = "application/json")
    public String getUserInfo(@PathVariable String username) {
        return producerTemplate.requestBody("direct:getUserInfo", username, String.class);

    }

    @PostMapping(value = "/generate-telegram-code", produces = "application/json")
    public ResponseEntity<String> generateTelegramCode(@Valid @RequestBody GenerateTelegramLinkRequest generateTelegramLinkRequest) {
        Exchange response = producerTemplate.request("direct:generateTelegramCode", exchange -> {
            exchange.getMessage().setBody(generateTelegramLinkRequest);
        });
        String body =  response.getMessage().getBody(String.class);
        Integer httpCode =  response.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);

        return ResponseEntity.status(httpCode != null ? httpCode : 200).body(body);
    }


}
