package com.ozon.online.entity;

import com.ozon.online.enums.ServiceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "another_service")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnotherService {
    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "com.ozon.online.util.CustomLongIdGenerator")
    private Long id;

    private ServiceStatus status;
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    List<String> links;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnotherService that = (AnotherService) o;
        return Objects.equals(id, that.id) && status == that.status && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, description);
    }

    @Override
    public String toString() {
        return "AnotherService{" +
                "id=" + id +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
