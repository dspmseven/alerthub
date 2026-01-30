package com.popelyshko.alerthub.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String firstName;
    private String lastName;


    @NotBlank
    private String email;

    private String phone;

}
