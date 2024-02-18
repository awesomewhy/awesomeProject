package com.ozon.online.entity;

import com.ozon.online.enums.OrderTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @NotNull
    private OrderTypeEnum orderTypeEnum;

}