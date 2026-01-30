package com.popelyshko.alerthub.service;

import com.popelyshko.alerthub.model.GenerateTelegramLinkRequest;
import com.popelyshko.alerthub.model.LinkCode;
import com.popelyshko.alerthub.model.User;
import com.popelyshko.alerthub.model.UserRequest;
import com.popelyshko.alerthub.repository.LinkCodeRepository;
import com.popelyshko.alerthub.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LinkCodeRepository linkCodeRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, LinkCodeRepository linkCodeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.linkCodeRepository = linkCodeRepository;
    }

    public boolean checkUserByUsername(String username) {
        return userRepository.existsByUsernameIsIgnoreCase(username);
    }

    public Long createUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        return userRepository.save(user).getId();
    }

    public Map<String, Object> getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        log.info(user.toString());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("email", user.getEmail());
        result.put("phone", user.getPhone());
        return result;
    }

    public boolean checkUserByUsernameAndPassword(GenerateTelegramLinkRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .map(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .orElse(false);
    }

    public String setTelegramLinkCodeToUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<LinkCode> existingCode = linkCodeRepository.findByUser(user);
        if (existingCode.isPresent()) {
            LinkCode linkCode = existingCode.get();
            if (linkCode.getExpiresAt().isAfter(LocalDateTime.now())) {
                return linkCode.getCode();
            }
            linkCode.setCode(UUID.randomUUID().toString());
            linkCode.setExpiresAt(LocalDateTime.now().plusMinutes(30));
            linkCodeRepository.save(linkCode);
            return linkCode.getCode();

        }
        String code = UUID.randomUUID().toString();
        LinkCode linkCode = new LinkCode();
        linkCode.setCode(code);
        linkCode.setUser(user);
        linkCode.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        linkCodeRepository.save(linkCode);
        return code;
    }

}
