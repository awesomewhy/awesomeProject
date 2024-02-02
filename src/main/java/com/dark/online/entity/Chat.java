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

//    @ManyToMany
//    @JoinTable(
//            name = "chat_participants",
//            joinColumns = @JoinColumn(name = "chat_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//    private List<User> participants;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User senderId;

    @ManyToOne
    @JoinColumn(name = "recipien_id")
    private User companionId;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany
    private List<Product_Image> images;
}