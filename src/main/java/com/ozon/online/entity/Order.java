package com.ozon.online.entity;

import com.ozon.online.enums.OrderTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    private LocalDateTime createdAt;;
    private String description;
    @NotNull
    private OrderTypeEnum orderType;
}
