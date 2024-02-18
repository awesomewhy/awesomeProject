package com.ozon.online.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_avatar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAvatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private String name;
    private String type;
    @Lob
    @Column(name = "image_data", length = 1000)
    private byte[] imageData;
}
