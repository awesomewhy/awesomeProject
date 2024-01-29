package com.dark.online.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToOne
    @JoinColumn(name = "photo_id")
    private Product productId;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chatId;

    private String name;
    private String type;
    @Lob
    @Column(name = "image_data", length = 1000)
    private byte[] imageData;
}