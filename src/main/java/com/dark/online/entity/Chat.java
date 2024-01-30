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
    private User userId;

    @ManyToOne
    private User companionId;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany
    private List<Product_Image> images;
}
