package com.dark.online.entity;

import com.dark.online.enums.OrderTypeEnum;
import com.dark.online.enums.PaymentTypeEnum;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private User sellerId;

    @OneToOne(mappedBy = "productId", cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id")
    private Product_Image photoId;

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
