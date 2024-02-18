package com.ozon.online.entity;

import com.ozon.online.enums.OrderTypeEnum;
import com.ozon.online.enums.PaymentTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedEntityGraph(name = "productWithSellerAndPhoto", attributeNodes = {
        @NamedAttributeNode("sellerId"),
        @NamedAttributeNode("photoId")
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User sellerId;

    @OneToOne(mappedBy = "productId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private ProductImage photoId;

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
}
