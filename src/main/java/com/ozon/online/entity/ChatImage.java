package com.ozon.online.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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
    private String name;
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;


    @Lob
    @Column(name = "image_data", length = 1000)
    private byte[] imageData;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatImage chatImage = (ChatImage) o;
        return Objects.equals(id, chatImage.id) && Objects.equals(name, chatImage.name) && Objects.equals(type, chatImage.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }

    @Override
    public String toString() {
        return "ChatImage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}