package com.ozon.online.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "news_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class News_Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_image_seq")
    @SequenceGenerator(name = "news_image_seq", sequenceName = "news_image_seq", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    private String name;
    private String type;

    @Lob
    @Column(name = "image_data", length = 1000)
    private byte[] imageData;
}

