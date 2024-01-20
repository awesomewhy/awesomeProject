package com.dark.online.entity;

import com.dark.online.enums.ProductTypeEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private ProductTypeEnum productTypeEnum;
    private Long price;
    private String description;
}
