package com.ozon.online.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "news_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "news_image_seq")
    @SequenceGenerator(name = "news_image_seq", sequenceName = "news_image_seq", allocationSize = 1)
    private Long id;
    private String name;
    private String type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @Lob
    @Column(name = "image_data", length = 1000)
    private byte[] imageData;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsImage newsImage = (NewsImage) o;
        return Objects.equals(id, newsImage.id) && Objects.equals(name, newsImage.name) && Objects.equals(type, newsImage.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }

    @Override
    public String toString() {
        return "News_Image{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

