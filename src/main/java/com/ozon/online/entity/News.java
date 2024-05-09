package com.ozon.online.entity;

import com.ozon.online.enums.NewsType;
import com.ozon.online.enums.ServiceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "news")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class News {
    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "com.ozon.online.util.CustomLongIdGenerator")
    private Long id;
    private String description;
    private NewsType type;
    private ServiceStatus status;

    @OneToOne(mappedBy = "news", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private NewsImage image;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> links;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(id, news.id) && Objects.equals(description, news.description) && type == news.type && status == news.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, type, status);
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}
