package com.dark.online.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "chat")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "companion_id")
    private User companionId;

    @OneToMany(mappedBy = "chatId", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "chatId", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "chatId", cascade = CascadeType.ALL)
    private List<Image> images;
}
