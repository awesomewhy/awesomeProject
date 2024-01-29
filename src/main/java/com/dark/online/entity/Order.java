package com.dark.online.entity;

import com.dark.online.enums.OrderTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User sellerId;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyerId;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chatId;

    private String name;
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal price;
    @NotNull
    private LocalDateTime createdAt;
    private String description;
    @NotNull
    private OrderTypeEnum orderType;
}
