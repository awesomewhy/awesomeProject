package com.ozon.online.entity;

import com.ozon.online.enums.NewsType;
import com.ozon.online.enums.ServiceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

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

    @OneToOne(mappedBy = "news", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private News_Image image;

    private String description;
    private NewsType type;
    private ServiceStatus status;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> links;
}
