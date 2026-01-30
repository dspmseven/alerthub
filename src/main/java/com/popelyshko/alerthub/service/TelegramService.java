package com.popelyshko.alerthub.service;

import com.popelyshko.alerthub.model.Notification;
import com.popelyshko.alerthub.model.User;
import com.popelyshko.alerthub.repository.LinkCodeRepository;
import com.popelyshko.alerthub.repository.NotificationRepository;
import com.popelyshko.alerthub.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
public class TelegramService {
    private final NotificationRepository notificationRepository;
    private final RestTemplate restTemplate;
    private final LinkCodeRepository linkCodeRepository;
    private final UserRepository userRepository;

    @Value("${telegram.auth-token}")
    private String botToken;

    public TelegramService(NotificationRepository notificationRepository, RestTemplate restTemplate, LinkCodeRepository linkCodeRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.restTemplate = restTemplate;
        this.linkCodeRepository = linkCodeRepository;
        this.userRepository = userRepository;
    }

    public String checkTelegramToken(Exchange exchange) {
        String token = exchange.getMessage().getBody(String.class);
        String chatId = exchange.getMessage().getHeader("CamelTelegramChatId", String.class);

        return linkCodeRepository.findByCode(token)
                .map(linkCode -> {
                    if(linkCode.getExpiresAt().isBefore(LocalDateTime.now())){
                        return "Code expired";
                    }

                    User user = linkCode.getUser();
                    user.setTelegramChatId(chatId);
                    userRepository.save(user);

                    linkCodeRepository.delete(linkCode);
                    return "Success! Telegram linked";
                }).orElse("Invalid Code");
    }


    public void sendTelegram(String chatId, String message){
        log.info("Sending Telegram Notification");
        try{
            String url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);

            Map<String, String> body = Map.of("chat_id", chatId, "text", message);

            restTemplate.postForObject(url, body, String.class);
            log.info("Sent Telegram Notification");
        } catch (Exception e){

            log.error(e.getMessage());
        }

        Notification notification = new Notification();
        notification.setName(chatId);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }
}
