package com.ozon.online.entity;

import com.ozon.online.enums.OrderTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @NotNull
    private OrderTypeEnum orderTypeEnum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name) && orderTypeEnum == category.orderTypeEnum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, orderTypeEnum);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", orderTypeEnum=" + orderTypeEnum +
                '}';
    }
}