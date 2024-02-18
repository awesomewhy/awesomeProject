package com.ozon.online.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private String name;
    private String type;

    @Lob
    @Column(name = "image_data", length = 1000)
    private byte[] imageData;
}