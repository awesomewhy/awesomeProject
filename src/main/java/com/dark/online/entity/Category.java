package com.dark.online.entity;

import com.dark.online.enums.OrderTypeEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private OrderTypeEnum orderTypeEnum;

}