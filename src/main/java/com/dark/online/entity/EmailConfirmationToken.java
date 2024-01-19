package com.dark.online.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Indexed;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_confirmation_tokens")
@Getter
@Setter
public class EmailConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String token;

    @CreatedDate
    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
