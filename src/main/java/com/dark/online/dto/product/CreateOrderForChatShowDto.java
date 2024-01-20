package com.dark.online.dto.product;

import com.dark.online.enums.OrderTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderForChatShowDto {
    private OrderTypeEnum orderTypeEnum;
    private BigDecimal price;
    private String description;
}
