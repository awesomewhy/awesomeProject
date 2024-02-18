package com.ozon.online.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
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
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "com.ozon.online.util.CustomLongIdGenerator")
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "chat_participants",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants;

//    @ManyToOne
//    @JoinColumn(name = "sender_id")
//    private User senderId;
//м
//    @ManyToOne
//    @JoinColumn(name = "recipien_id")
//    private User companionId;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;

    private LocalDateTime createdAt;

    @OneToMany
    private List<ProductImage> images;
}