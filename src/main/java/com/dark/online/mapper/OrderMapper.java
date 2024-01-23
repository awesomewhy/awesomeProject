package com.dark.online.mapper;

import com.dark.online.dto.order.CreateOrderForChatShowDto;
import com.dark.online.entity.Order;
import com.dark.online.entity.User;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order mapCreateOrderForChatShowDtoToEntity(User id, CreateOrderForChatShowDto createOrderForChatShowDto, User user) {
//        OrderTypeEnum typeEnum = OrderTypeEnum.DEFAULT.ordinal();
//        for (OrderTypeEnum type : OrderTypeEnum.values()) {
//            if (type.ordinal() == createOrderForChatShowDto.getOrderTypeEnum().ordinal()) {
//                typeEnum = type.ordinal();
//            }
//        }
        Order order = Order.builder()
                .sellerId(id)
                .buyerId(user)
                .orderType(createOrderForChatShowDto.getOrderTypeEnum())
                .price(createOrderForChatShowDto.getPrice())
                .description(createOrderForChatShowDto.getDescription())
                .build();
        return order;
    }
}
