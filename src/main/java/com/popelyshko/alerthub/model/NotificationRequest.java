package com.popelyshko.alerthub.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    @NotBlank
    private String name;

    private String message;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private Severity severity;

    @NotBlank
    private String source;
    private String tags;
    private boolean resolved;

}
