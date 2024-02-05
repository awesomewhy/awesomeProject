package com.dark.online.entity;

import com.dark.online.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
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
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "com.dark.online.util.CustomLongIdGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chatId;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

    private String message;
    private MessageStatus messageStatus;
    private Timestamp createdAt;
}