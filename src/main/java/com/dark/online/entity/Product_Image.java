package com.dark.online.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product_Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product productId;

    private String name;
    private String type;
    @Lob
    @Column(name = "image_data", length = 1000)
    private byte[] imageData;
}