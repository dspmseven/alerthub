package com.popelyshko.alerthub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String telegramChatId;
}
