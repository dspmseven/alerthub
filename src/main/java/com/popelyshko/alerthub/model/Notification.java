package com.popelyshko.alerthub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String message;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    private String source;
    private String tags;
    private boolean resolved;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
