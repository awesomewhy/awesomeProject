package com.ozon.online.entity;

import com.ozon.online.enums.OrderTypeEnum;
import com.ozon.online.enums.PaymentTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "5.0", inclusive = true)
    private BigDecimal rating;
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal price;
    private BigDecimal discount;
    private LocalDateTime createdAt;
    private String description;
    private OrderTypeEnum orderType;
    private PaymentTypeEnum paymentType;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User sellerId;

    @OneToOne(mappedBy = "productId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private ProductImage photoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(rating, product.rating) && Objects.equals(price, product.price) && Objects.equals(discount, product.discount) && Objects.equals(createdAt, product.createdAt) && Objects.equals(description, product.description) && orderType == product.orderType && paymentType == product.paymentType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, rating, price, discount, createdAt, description, orderType, paymentType);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", price=" + price +
                ", discount=" + discount +
                ", createdAt=" + createdAt +
                ", description='" + description + '\'' +
                ", orderType=" + orderType +
                ", paymentType=" + paymentType +
                '}';
    }
}
