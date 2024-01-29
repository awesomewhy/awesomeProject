package com.dark.online.entity;

import com.dark.online.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chatId;

    @OneToOne
    private User sender;

    @OneToOne
    private User recipient;

    private String message;
    private LocalDateTime time;
    private MessageStatus messageStatus;
}