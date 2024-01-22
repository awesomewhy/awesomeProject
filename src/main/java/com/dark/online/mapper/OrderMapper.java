package com.dark.online.mapper;

import com.dark.online.dto.product.CreateOrderForChatShowDto;
import com.dark.online.dto.product.CreateOrderForSellDto;
import com.dark.online.dto.product.OrderForShowDto;
import com.dark.online.entity.Order;
import com.dark.online.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderMapper {
    public Order mapCreateOrderForSellDtoToOrderEntity(CreateOrderForSellDto createOrderForSellDto, User user) {
        Order order = Order.builder()
                .sellerId(user)
                .name(createOrderForSellDto.getName())
                .image(createOrderForSellDto.getImage())
                .price(createOrderForSellDto.getPrice())
                .discount(createOrderForSellDto.getPrice().add(createOrderForSellDto.getPrice()))
                .rating(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .description(createOrderForSellDto.getDescription())
                .orderType(createOrderForSellDto.getOrderTypeEnum())
                .paymentType(createOrderForSellDto.getPaymentTypeEnum())
                .build();
        return order;
    }

    public Order mapCreateOrderForChatShowDtoToEntity(CreateOrderForChatShowDto createOrderForChatShowDto, User user) {
//        OrderTypeEnum typeEnum = OrderTypeEnum.DEFAULT.ordinal();
//        for (OrderTypeEnum type : OrderTypeEnum.values()) {
//            if (type.ordinal() == createOrderForChatShowDto.getOrderTypeEnum().ordinal()) {
//                typeEnum = type.ordinal();
//            }
//        }
        Order order = Order.builder()
                .sellerId(user)
                .orderType(createOrderForChatShowDto.getOrderTypeEnum())
                .price(createOrderForChatShowDto.getPrice())
                .description(createOrderForChatShowDto.getDescription())
                .build();
        return order;
    }

    public OrderForShowDto mapOrderToOrderForShowDto(Order order) {
        return OrderForShowDto.builder()
                .id(order.getId())
                .name(order.getName())
                .rating(order.getRating())
                .build();
    }
}
